(ns indexr.app
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

(defn add-field [document field-key field-value]
  (.add document (TextField. field-key field-value)))

(defn add-document [writer file]
  (let [document (Document.)]
    (with-open [reader (io/reader file)]
      (add-field document "contents" reader))))

(defn index-file [index-path file]
  (let [idx-dir (FSDirectory/open index-path)
        analyzer (StandardAnalyzer.)
        idx-cfg (.setOpenMode (IndexWriterConfig. analyzer) IndexWriterConfig$OpenMode/CREATE_OR_APPEND)]
    (with-open [idx-writer (IndexWriter. idx-dir idx-cfg)]
      (add-document idx-writer file))))

(defn index-search [index-path fieldname criteria]
  (let [reader (DirectoryReader/open (FSDirectory/open index-path))
        analyzer (StandardAnalyzer.)
        searcher (IndexSearcher. reader)
        parser (QueryParser. fieldname analyzer)
        query (.parse parser criteria)]
    (.search searcher query 10)))

(defn run [opt]
  (index-file (:index opt) (:file opt)))

#_(def idx-path (.toPath (io/file "/home/carlos/idx")))
#_(def file1 (io/file "/home/carlos/d1.txt"))
#_(def file2 (io/file "/home/carlos/d2.txt"))

#_(index-file idx-path file1)
#_(index-file idx-path file2)
#_(def results (index-search (.toPath (io/file "/home/carlos/idx")) "contents" "fish"))

#_(.totalHits results)
