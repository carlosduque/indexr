(ns indexr.app
  (:import [java.util.concurrent LinkedBlockingQueue])
  (:require
    [clojure.java.io :as io]
    [indexr.index :as i]
    [indexr.search :as s])
  (:gen-class))

(defn run [opt]
  (enqueue-file-work-items (create-queue) (:directory opt)))

(defn create-queue
  ([] (LinkedBlockingQueue.)))

(defn enqueue [^LinkedBlockingQueue queue work-item]
  (try
    (.put queue work-item)
  (catch Exception e
    (str "caught exception while putting to queue" (.getMessage e)))))

(defn readable-file? [f]
  (and (.canRead f) (.isFile f)))

(defn walk
  ([dirpath]
    (walk dirpath identity))
  ([dirpath pred]
    (doall
      (filter pred (file-seq (io/file dirpath))))))

(enqueue-file-work-items [queue src-dir]
  (map #(enqueue queue %) (map create-work-item (walk src-dir readable-file?)))

(defn process-files [filelist]
  (let [agents (doall (map #(agent %) filelist))]
    (doseq [agent agents]
      (send-off agent i/index-file (:index opt) (datafy agent) ))
    (apply await-for 5000 agents)
    (doall (map #(deref %) agents))))

(defn create-work-item [filepath]
  (let [file (io/file filepath)]
    (when (and (.canRead file) (.isFile file))
      {:path (.getCanonicalPath file)
       :file file
       :created-at (System/currentTimeMillis)})))

