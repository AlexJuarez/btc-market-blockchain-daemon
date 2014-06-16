(ns warden.cache
  (:refer-clojure :exclude [set get])
  (:require [clojurewerkz.spyglass.client :as c]
            [noir.util.cache :as cache]))

(def ^:private address "127.0.0.1:11211")

(defonce ce (c/text-connection address))

(defn set [key value & opts]
  (let [ttl (or (first opts)
                (+ (* 60 10) (rand-int 600)))];;Prevent stampede
    (c/set ce key ttl value)))

(defn get [key]
  (c/get ce key))

(defn delete [key]
  (cache/invalidate! key)
  (c/delete ce key))
