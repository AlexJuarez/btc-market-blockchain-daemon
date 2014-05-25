(ns warden.core
  (:use
   [overtone.at-at])
  (:require
   [clj-btc.core :as btc]
   [warden.tasks.exchange :as exchange]
   [warden.models.currency :as currency]))

(def pool (mk-pool))

(defn foo []
  (every 5000 #(println "I am cool!") pool))

(def config {:rpcpassword "whitecity"
             :rpcuser "devil"
             :rpchost "http://127.0.0.1"
             :rpcport "8332"
             })
