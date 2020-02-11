(ns indexr.index
  (:require
    [clojure.java.io :as io])
  (:import
    [org.apache.lucene.analysis Analyzer]
    [org.apache.lucene.analysis.standard StandardAnalyzer]
    [org.apache.lucene.document Document Field Field$Store StringField TextField]
    [org.apache.lucene.index IndexWriter IndexWriterConfig IndexWriterConfig$OpenMode
                             DirectoryReader]
    [org.apache.lucene.queryparser.classic QueryParser]
    [org.apache.lucene.search IndexSearcher Query ScoreDoc]
    [org.apache.lucene.store Directory FSDirectory])
  (:gen-class))

(defn to-str [value]
  (if (keyword? value)
    (name value)
    (str value)))

(defn add-field [document k v]
  (if (= k :contents)
    (.add document (TextField. (to-str k) v))
    (.add document (StringField. (to-str k) (to-str v) Field$Store/YES))))

(defn fields->doc [fields]
  (let [document (Document.)]
    (doseq [[k v] fields]
      (add-field document k v))
    document))

(defn index-writer [index-path]
  (let [dir (FSDirectory/open index-path)
        analyzer (StandardAnalyzer.)
        cfg (IndexWriterConfig. analyzer)]
    (IndexWriter. dir (.setOpenMode cfg IndexWriterConfig$OpenMode/CREATE_OR_APPEND))))

(defn index-file [index-path fields]
  (with-open [writer (index-writer index-path)
              reader (io/reader (:file fields))]
      (doseq [f fields]
        (.addDocument writer (fields->doc (assoc fields :contents reader))))))

;;;;;;;
#_(def idx-path (.toPath (io/file "/Users/carlos/idx")))
#_(def idx-big (.toPath (io/file "/Users/carlos/index-big")))
#_(def idx-small (.toPath (io/file "/Users/carlos/index-small")))

#_(def book1 {:path "/Users/carlos/dev/src/indexr/resources/books/d1.txt"
            :file (io/file "./resources/books/d1.txt")
            :created-at (System/currentTimeMillis)})

#_(def book2 {:path "/Users/carlos/dev/src/indexr/resources/books/d2.txt"
            :file (io/file "./resources/books/d2.txt")
            :created-at (System/currentTimeMillis)})

#_(index-file idx-path book1)
#_(index-file idx-path book2)
