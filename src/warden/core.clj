(ns warden.core
  (:use
   [overtone.at-at]
   [environ.core :only [env]])
  (:require
   [warden.tasks.exchange :as exchange]
   [warden.tasks.deposit :as deposit]
   [warden.tasks.withdrawal :as withdrawal]
   [warden.models.currency :as currency]))

(def pool (mk-pool))

(defn update-exchange []
  (every 600000 exchange/update-from-remote pool
         :fixed-delay true
         :desc "updates bitcoin prices from coinbase"))

(defn check-wallets []
  (every 600000 deposit/start-task pool
         :fixed-delay true
         :desc "checks for deposits"))

(defn check-withdrawals []
    (every 600000 withdrawal/start-task pool
         :fixed-delay true
         :desc "checks for withdrawals"))

(defn -main []
  (update-exchange)
  (check-wallets)
  (check-withdrawals))
