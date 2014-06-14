(ns warden.db
  (:use korma.core
        [korma.db :only (defdb postgres)]
        [korma.core]))

(def wallets-spec
  {:subprotocol "postgresql"
   :subname "//localhost/wallets"
   :user "warden"
   :password "admin"})

(def web-db
  (postgres
    {:subname "//localhost/whitecity"
     :user "devil"
     :password "admin"}))

(defdb web web-db)

(declare users audits exchange currency withdrawal transactions blocks)

(defentity users
  (database web)
  (table :user))

(defentity exchange
  (database web)
  (table :exchangerate))

(defentity currency
  (database web)
  (table :currency))

(defentity withdrawal
  (database web)
  (table :withdrawal))

(defentity audits
  (database web)
  (table :audit))

(defdb secure wallets-spec)

(defentity transactions
  (database secure)
  (table :transaction))

(defentity blocks
  (database secure)
  (table :block))
