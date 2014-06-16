(defproject
  warden
  "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :profiles {:user
             {:env
              {:confrimations 5
               :wallet-db-user "warden"
               :wallet-db-pass "admin"}}}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies
  [[org.clojure/clojure "1.6.0"]
   [lib-noir "0.8.2"];;for io and session utils
   [overtone/at-at "1.2.0"]
   [log4j
    "1.2.17"
    :exclusions
    [javax.mail/mail
     javax.jms/jms
     com.sun.jdmk/jmxtools
     com.sun.jmx/jmxri]]
   [environ "0.4.0"]
   [clj-http "0.7.8"];;for reading bitcoins prices from coinbase.com
   [org.clojure/java.jdbc "0.3.3"];;dependency for korma
   [postgresql/postgresql "9.1-901.jdbc4"]
   [korma "0.3.2"]
   [clj-btc "0.1.1"]]
  :main warden.core)
