(ns warden.core
  (:use
   [korma.db :only (transaction)]
   [korma.core]
   [warden.db]
   [overtone.at-at]
   [environ.core :only [env]])
  (:require
   [clj-btc.core :as btc]
   [clojure.string :as s]
   [warden.tasks.exchange :as exchange]
   [warden.models.currency :as currency]))

(def pool (mk-pool))

(defn foo []
  (every 5000 #(println "I am cool!") pool))

(defn parse-int [s]
  (if (string? s)
    (let [i (re-find #"\d+" s)]
      (if-not (s/blank? i)
        (Integer. i)
        0))
    s))

(defn process [tx]
  (let [info (btc/gettransaction tx)]
    (if (and (= (get-in info ["details" "category"]) "receive")
             (> (info "confirmations") (env :confrimations)))
      (let [user-id (parse-int (get-in info ["details" "account"]))
            amount (parse-int (info "amount"))]
        (transaction 
          (insert audits (values {:amount amount :tx (info "txid") :role "deposit" :user_id user-id}))
          (update users (set-fields {:btc (raw (str "btc + " amount))}) (where {:id user-id})))))))
  

(defn updatewallets
  (let [txs (select transactions
                       (limit 1000) (order :created_on :desc))]
    (doall (map (-> :id process) txs))))


(def config {:rpcpassword "whitecity"
             :rpcuser "devil"
             :rpchost "http://127.0.0.1"
             :rpcport "8332"})
