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

#_(defn add-field [document field-key field-value]
  (.add document (TextField. field-key field-value)))

#_(defn add-document [writer fields]
  (let [document (Document.)]
    (add-field document "contents" reader)))

(defn index-file [index-path fields]
  (let [dir (FSDirectory/open index-path)
        analyzer (StandardAnalyzer.)
        cfg (.setOpenMode (IndexWriterConfig. analyzer) IndexWriterConfig$OpenMode/CREATE_OR_APPEND)
        fields {:file-reader (io/reader file)}]
    (with-open [writer (IndexWriter. dir cfg)]
      (let [document (Document.)]
        (do
          (println "adding fields")
          (.add document (StringField. "k1" "fish" Field$Store/YES))
          (.add document (StringField. "k2" "bear" Field$Store/YES))
          (.add document (StringField. "k3" "shark" Field$Store/YES))
          (.add document (StringField. "k4" "eagle" Field$Store/YES))
          (.add document (StringField. "k5" "wolf" Field$Store/YES))
          (println "all added")
          (println "writing...")
          (.addDocument writer document))))))

;;;;;;;
(def idx-path (.toPath (io/file "/Users/carlos/idx")))
#_(def idx-big (.toPath (io/file "/Users/carlos/index-big")))
#_(def idx-small (.toPath (io/file "/Users/carlos/index-small")))
(def file1 (io/file "./resources/books/d1.txt"))
(index-file idx-path file1)

(def file2 (io/file "./resources/books/d2.txt"))
(index-file idx-path file2)

