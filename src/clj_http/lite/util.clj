(ns clj-http.lite.util
  "Helper functions for the HTTP client."
  (:require [clojure.java.io :as io])
  (:import (java.io ByteArrayInputStream ByteArrayOutputStream)
           (java.net URLEncoder URLDecoder)
           (java.util.zip InflaterInputStream DeflaterInputStream
                          GZIPInputStream GZIPOutputStream)))

(defn utf8-bytes
  "Returns the UTF-8 bytes corresponding to the given string."
  [#^String s]
  (.getBytes s "UTF-8"))

(defn utf8-string
  "Returns the String corresponding to the UTF-8 decoding of the given bytes."
  [#^"[B" b]
  (String. b "UTF-8"))

(defn url-decode
  "Returns the form-url-decoded version of the given string, using either a
  specified encoding or UTF-8 by default."
  [encoded & [encoding]]
  (URLDecoder/decode encoded (or encoding "UTF-8")))

(defn url-encode
  "Returns an UTF-8 URL encoded version of the given string."
  [unencoded]
  (URLEncoder/encode unencoded "UTF-8"))

(defn base64-encode
  "Encode an array of bytes into a base64 encoded string."
  [unencoded]
  (javax.xml.bind.DatatypeConverter/printBase64Binary unencoded))

(defn to-byte-array
  "Returns a byte array for the InputStream provided."
  [is]
  (let [chunk-size 8192
        baos (ByteArrayOutputStream.)
        buffer (byte-array chunk-size)]
    (loop [len (.read is buffer 0 chunk-size)]
      (when (not= -1 len)
        (.write baos buffer 0 len)
        (recur (.read is buffer 0 chunk-size))))
    (.toByteArray baos)))


(defn gunzip
  "Returns a gunzip'd version of the given byte array."
  [b]
  (when b
    (to-byte-array (GZIPInputStream. (ByteArrayInputStream. b)))))

(defn gzip
  "Returns a gzip'd version of the given byte array."
  [b]
  (when b
    (let [baos (ByteArrayOutputStream.)
          gos  (GZIPOutputStream. baos)]
      (io/copy (ByteArrayInputStream. b) gos)
      (.close gos)
      (.toByteArray baos))))

(defn inflate
  "Returns a zlib inflate'd version of the given byte array."
  [b]
  (when b
    (to-byte-array (InflaterInputStream. (ByteArrayInputStream. b)))))

(defn deflate
  "Returns a deflate'd version of the given byte array."
  [b]
  (when b
    (to-byte-array (DeflaterInputStream. (ByteArrayInputStream. b)))))
