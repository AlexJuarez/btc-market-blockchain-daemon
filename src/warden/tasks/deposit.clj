(ns warden.tasks.deposit
  (:use
   [korma.db :only (transaction)]
   [korma.core]
   [warden.db]
   [environ.core :only [env]])
  (:require
   [clj-btc.core :as btc]
   [warden.cache :as cache]
   [warden.util :as util]))

(def config {:rpcpassword "whitecity"
             :rpcuser "devil"
             :rpchost "http://127.0.0.1"
             :rpcport "8332"})

(defn handle-details [{account "account" category "category" amount "amount"} txid]
  (if (= category "receive")
      (let [user-id (util/parse-int account)
            user (update users (set-fields {:btc (raw (str "btc + " amount))}) (where {:id user-id}))]
        (do
          (when-not (and (nil? user) (nil? (:session user)))
            (let [session (.toString (:session user))
                  data (cache/get session)]
              (cache/set session (assoc-in data [:noir :user :btc] (:btc user)))
              (println (:btc user))))
          (insert audits (values {:amount amount :tx txid  :role "deposit" :user_id user-id})))
        )))

(defn process [tx]
  (let [info (btc/gettransaction :txid tx :config config)
        details (info "details")
        confirmations (info "confirmations")]
    (if (>= confirmations 6)
      (do
       (update transactions (set-fields {:processed true}) (where {:id tx}))
       (doall (map #(handle-details % tx) details))))))

(defn start-task []
  (let [txs (select transactions (where {:processed false}) (limit 1000) (order :created_on :desc))]
    (doall (map #(process (:id %)) txs))))
