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

(declare exchange currency withdrawal)

(defentity exchange
  (table :exchangerate))

(defentity currency
  (table :currency))

(defentity withdrawal
  (table :withdrawal))
