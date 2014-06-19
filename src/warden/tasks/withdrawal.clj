(ns warden.tasks.withdrawal
  (:use
   [korma.db :only (transaction)]
   [korma.core]
   [warden.db])
  (:require
   [clj-btc.core :as btc]
   [warden.util :as util]))

(def config {:rpcpassword "whitecity"
             :rpcuser "devil"
             :rpchost "http://127.0.0.1"
             :rpcport "8332"})

(defn process [{:keys [amount user_id address id]}]
  (let [amt (:amt (first (select audits (fields :user_id) (aggregate (sum :amount) :amt) (group :user_id) (where {:user_id user_id}))))]
    (if (>= amt 0)
      (do
        (btc/sendtoaddress :bitcoinaddress address :amount amount)
        (update withdrawal (set-fields {:processed true}) (where {:id id})))
      (update withdrawal (set-fields {:locked true}) (where {:id id})))))

(defn start-task []
  (let [w (select withdrawal (order :created_on :desc))]
    (when (> (count w) 0)
      (loop [withdrawals w
             balance (btc/getbalance :config config)]
        (let [w (first withdrawals)]
          (when-not (or (<= (count withdrawals) 0)
                     (<= (- balance (:amount w)) 0))
            (process w)
            (recur (rest withdrawals) (- balance (:amount w)))))))))
