(ns warden.core
  (:use [overtone.at-at])
  (:require
   [warden.tasks.exchange :as exchange]
   [warden.models.currency :as currency]))

(def pool (mk-pool))

(defn foo []
  (every 5000 #(println "I am cool!") pool))
