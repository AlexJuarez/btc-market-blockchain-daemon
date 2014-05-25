(ns warden.models.currency
  (:refer-clojure :exclude [get find])
  (:require
        [korma.core :refer [where values select insert]]
        [korma.db :refer [defdb]])
  (:use [warden.db :only [currency]]))

(defn get [id]
  (first
    (select currency
            (where {:id id}))))

(defn all []
  (select currency))

(defn add! [currencies]
  (insert currency (values currencies)))

(defn find [name]
  (first
    (select currency
            (where {:key name}))))
