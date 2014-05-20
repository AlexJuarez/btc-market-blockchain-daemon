(ns warden.cache
  (:refer-clojure :exclude [set get])
  (:require [clojurewerkz.spyglass.client :as c]
            [noir.util.cache :as cache]))

(def ^:private address "127.0.0.1:11211")

(defonce ce (c/text-connection address))

(defn set [key value & ttl]
  (c/set ce key (or (first ttl) (+ (* 60 10) (rand-int 600))) value)) ;;Prevent stampede

(defn get [key]
  (c/get ce key))

(defn delete [key]
  (cache/invalidate! key)
  (c/delete ce key))

(defmacro cache! [key & forms]
  `(cache/cache! ~key
                 (let [value# (get ~key)]
                   (if (nil? value#)
                     (let [v# (do ~@forms)]
                       (set ~key v#)
                       v#)
                     value#))))
