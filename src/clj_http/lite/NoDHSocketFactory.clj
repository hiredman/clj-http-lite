(ns clj-http.lite.NoDHSocketFactory
  (:import (javax.net.ssl SSLSocket SSLSocketFactory)
           (java.net Socket))
  (:gen-class
    :name clj-http.lite.NoDHSocketFactory
    :extends javax.net.ssl.SSLSocketFactory
    :init init
    :state state
    :constructors {[javax.net.ssl.SSLSocketFactory] []}))

(defn strip-dh-suites
  "Remove cipher suites containing 'DH'"
  [suites]
  (into-array String (filter #(not (or (re-find #"_DHE_" %)
                                       (re-find #"_DH_" %)
                                       (re-find #"_ECDH_" %)
                                       (re-find #"_ECDHE_" %))) suites)))

(defn -init
  [^SSLSocketFactory f]
  (let [state {:factory f
               :enabled-ciphers (strip-dh-suites (.getSupportedCipherSuites f))}]

    [[] (atom state)]))

(defn -createSocket [this & args]
  (doto
    (apply (partial (memfn createSocket) (:factory @(.state this))) args)
    (.setEnabledCipherSuites (:enabled-ciphers @(.state this)))))

(defn -getDefaultCipherSuites [this]
  (strip-dh-suites (.getDefaultCipherSuites (:factory @(.state this)))))

(defn -getSupportedCipherSuites [this]
  (strip-dh-suites (.getSupportedCipherSuites (:factory @(.state this)))))
