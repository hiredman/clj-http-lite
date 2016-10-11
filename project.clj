(defproject clj-http-lite "0.4.0-SNAPSHOT"
  :description "A Clojure HTTP library similar to clj-http, but more lightweight."
  :url "https://github.com/hiredman/clj-http-lite/"
  :repositories {"sona" "http://oss.sonatype.org/content/repositories/snapshots"}
  :warn-on-reflection false
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [slingshot "0.12.2"]]
  :profiles {:dev {:dependencies [[ring/ring-jetty-adapter "1.5.0"]
                                  [ring/ring-devel "1.5.0"]]}
             :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.0"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}}
  :test-selectors {:default  #(not (:integration %))
                   :integration :integration
                   :all (constantly true)}
  :aliases {"all" ["with-profile" "dev,1.4:dev,1.5:dev,1.6:dev,1.7:dev,1.8"]}
  :checksum-deps true)
