(ns clj-http.lite.NoDHSocketFactory
  (:import (javax.net.ssl SSLSocket SSLSocketFactory)
           (java.net Socket)))

(defn strip-dh-suites
  "Remove cipher suites containing 'DH'"
  [suites]
  (into-array String (filter #(not (or (re-find #"_DHE_" %)
                                       (re-find #"_DH_" %)
                                       (re-find #"_ECDH_" %)
                                       (re-find #"_ECDHE_" %))) suites)))

(defn set-cipher-suites [s sf]
  (.setEnabledCipherSuites s (strip-dh-suites (.getSupportedCipherSuites sf)))
  s)

(defn no-dhs-socket-factory [sf]
  (proxy [SSLSocketFactory] []
    (createSocket
      ([]
         (doto (.createSocket sf)
           (set-cipher-suites sf)))
      ([host port]
         (doto (.createSocket sf host port)
           (set-cipher-suites sf)))
      ([host port local-host local-port]
         (doto (.createSocket sf host port local-host local-port)
           (set-cipher-suites sf))))
    (getDefaultCipherSuites []
      (.getDefaultCipherSuites sf))
    (getSupportedCipherSuites []
      (.getSupportedCipherSuites sf))))
