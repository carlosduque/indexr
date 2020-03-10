(ns indexr.app
  (:import [java.util.concurrent LinkedBlockingQueue])
  (:require
    [clojure.java.io :as io]
    [indexr.index :as i]
    [indexr.search :as s])
  (:gen-class))

#_(defn create-queue
  ([] (LinkedBlockingQueue.)))

(defn create-queue []
  (atom []))

#_(defn enqueue [^LinkedBlockingQueue queue work-item]
  (try
    (.put queue work-item)
  (catch Exception e
    (str "caught exception while putting to queue" (.getMessage e))))
  queue)

(defn enqueue [queue work-item]
  (swap! queue conj work-item))

(defn readable-file? [f]
  (and (.canRead f) (.isFile f)))

(defn walk
  ([dirpath]
    (walk dirpath identity))
  ([dirpath pred]
    (doall
      (filter pred (file-seq (io/file dirpath))))))

(defn create-work-item [file]
  {:path (.getCanonicalPath file)
   :file file
   :created-at (System/currentTimeMillis)})

(defn enqueue-file-work-items [queue src-dir]
  (map #(enqueue queue %)
       (map create-work-item (walk src-dir readable-file?))))

(defn process-items [index items]
  (with-open [idx-writer (i/index-writer index)]
    (let [agents (doall (map #(agent %) items))]
      (doseq [agent agents]
        (send-off agent i/index-file idx-writer))
      (apply await-for 5000 agents)
      agents)))

(defn run [opt]
  (let [queue (create-queue)
        src-dir (:directory opt)
        index-dir (:index opt)]
    (enqueue-file-work-items queue src-dir)
    (process-items index-dir @queue)))

;;;;
#_(require '[clojure.java.io :as io])
#_(def books (io/file "./resources/books"))
#_(def idx (io/file "/Users/carlos/idx"))
#_(def q (create-queue))
#_(def params {:directory books
             :index (.toPath idx)})

;;Tika
;;https://github.com/rickcrawford/lucene-example/blob/master/src/main/java/WriteIndex.java
