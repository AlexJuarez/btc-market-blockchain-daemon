(ns warden.notify
  (:require 
    [taoensso.timre :as timbre])
  (:gen-class))

(defn wallet? [args] (some #{"-wallet"} args))

(defn -main [& args]
  (timbre/info "notification started"))
