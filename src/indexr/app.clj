(ns indexr.app
  (:import [java.util.concurrent LinkedBlockingQueue])
  (:require
    [clojure.java.io :as io]
    [indexr.index :as i]
    [indexr.search :as s])
  (:gen-class))

(defn run [opt]
  (build-work-load (create-queue) (:index opt) (:file opt)))

(defn create-queue
  ([] (LinkedBlockingQueue.)))

(defn enqueue [^LinkedBlockingQueue queue index filepath]
  (let [file (io/file filepath)]
    (when (and (.canRead file) (.isFile file))
             (.put queue (datafy file)))))

(defn process-files [filelist]
  (let [agents (doall (map #(agent %) filelist))]
    (doseq [agent agents]
      (send-off agent i/index-file (:index opt) (datafy agent) ))
    (apply await-for 5000 agents)
    (doall (map #(deref %) agents))))

(defn datafy [file]
    {:path (.getCanonicalPath file)
     :file file
     :created-at (System/currentTimeMillis)})

