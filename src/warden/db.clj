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

(declare exchange currency)

(defentity exchange
  (database web)
  (table :exchangerate))

(defentity currency
  (database web)
  (table :currency))
