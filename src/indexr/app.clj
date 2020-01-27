(ns indexr.app
  (:require
    [clojure.java.io :as io]
    [indexr.index :as i]
    [indexr.search :as s])
  (:gen-class))

(defn run [opt]
  (i/index-file (:index opt) (:file opt)))

