(ns indexr.app
  (:import
    (org.apache.lucene.search IndexSearcher Query))
  (:gen-class))

(defn run [opt arg err]
  (println (str "opt: " opt " \narg: " arg " \nerr: " err)))

