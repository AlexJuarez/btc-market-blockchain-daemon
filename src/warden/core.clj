(ns warden.core
  (:use
   [overtone.at-at]
   [environ.core :only [env]])
  (:require
   [taoensso.timbre :as timbre]
   [com.postspectacular.rotor :as rotor]
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
  (timbre/set-config!
   [:appenders :rotor]
   {:min-level :info
    :enabled? true
    :async? false ; should be always false for rotor
    :max-message-per-msecs nil
    :fn rotor/append})

  (timbre/set-config!
   [:shared-appender-config :rotor]
   {:path "warden.log" :max-size (* 512 1024) :backlog 10})
  (update-exchange)
  (check-wallets)
  (check-withdrawals))
