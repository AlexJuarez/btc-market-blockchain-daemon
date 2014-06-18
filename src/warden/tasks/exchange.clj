(ns warden.tasks.exchange
  (:use
   [warden.db :only [exchange]]
   [korma.core]
   [clojure.string :only (split lower-case)])
  (:require
   [warden.models.currency :as currency]
   [clj-http.client :as client]))

(defn update-from-remote []
  (let [response ;;(jr/parse-string (slurp "resources/exchange_rates.json"))
              (:body (client/get "https://coinbase.com/api/v1/currencies/exchange_rates"
              {:conn-timeout 1000
               :content-type :json
               :follow-redirects false
               :as :json
               :accept :json}))
        currencies (apply merge (map #(assoc {} (lower-case (:key %)) (:id %)) (currency/all)))
        prep (filter #(not (or (nil? (:from %)) (nil? (:to %)))) (map #(let [s (split (name (key %)) #"_")] {:from (currencies (.substring (first s) 0 3)) :to (currencies (.substring (last s) 0 3)) :value (Float/parseFloat (val %))}) response))]
    (if-not (empty? response)
      (do
        (println "updating from remote")
        (delete exchange)
        (insert exchange
                (values prep))))))
