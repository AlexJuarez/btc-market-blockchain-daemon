(ns warden.tasks.deposit
  (:use
   [korma.db :only (transaction)]
   [korma.core]
   [warden.db]
   [environ.core :only [env]])
  (:require
   [clj-btc.core :as btc]
   [warden.util :as util]))

(def config {:rpcpassword "whitecity"
             :rpcuser "devil"
             :rpchost "http://127.0.0.1"
             :rpcport "8332"})

(defn handle-details [{account "account" category "category" amount "amount"} txid]
  (if (= category "receive")
      (let [user-id (util/parse-int account)
            amount (util/parse-int amount)]
        (transaction
          (insert audits (values {:amount amount :tx txid  :role "deposit" :user_id user-id}))
          (update users (set-fields {:btc (raw (str "btc + " amount))}) (where {:id user-id}))))))

(defn process [tx]
  (let [info (btc/gettransaction :txid tx :config config)
        details (info "details")
        confirmations (info "confirmations")]
    (if (> confirmations 6)
      (doall (map #(handle-details % tx) details)))))

(defn updatewallets []
  (let [txs (select transactions (limit 1000) (order :created_on :desc))]
    (doall (map (-> :id process) txs))))

(def tx (:id (second (select transactions))))

(process tx)

((btc/gettransaction :txid tx :config config) "details")
