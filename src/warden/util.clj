(ns warden.util
  (:require
    [warden.cache :as cache]
    [clojure.string :as s]))

(defn parse-int [s]
  (if (string? s)
    (let [i (re-find #"\d+" s)]
      (if-not (s/blank? i)
        (Integer. i)
        0))
    s))

(defmacro update-session
  [session & terms]
  `(let [sess# (cache/get session#)
        ttl# (* 60 60 10)]
    (if (not (nil? sess#))
      (cache/set session#
                 (assoc sess# :noir (dissoc (:noir sess#) ~@terms :user)) ttl#))))
