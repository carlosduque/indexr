(ns indexr.app
  (:import
    (org.apache.lucene.search IndexSearcher Query))
  (:gen-class))

(defn- directory-reader
  [path]
  (DirectoryReader/open (path)))

(defn run [opt]
  (println (str "opt: " opt)))

