(ns warden.core
  (:use
   [overtone.at-at]
   [environ.core :only [env]])
  (:require
   [warden.tasks.exchange :as exchange]
   [warden.tasks.deposit :as deposit]
   [warden.models.currency :as currency]))

(def pool (mk-pool))

(defn update-exchange []
  (every 600000 exchange/update-from-remote pool
         :fixed-delay true
         :desc "updates bitcoin prices from coinbase"))

(defn check-wallets []
  (every 600000 deposit/update-wallets pool
         :fixed-delay true
         :desc "checks for deposits"))

(defn -main []
  (update-exchange))
