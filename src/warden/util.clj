(ns warden.util
  (:require
    [clojure.string :as s]))

(defn parse-int [s]
  (if (string? s)
    (let [i (re-find #"\d+" s)]
      (if-not (s/blank? i)
        (Integer. i)
        0))
    s))
