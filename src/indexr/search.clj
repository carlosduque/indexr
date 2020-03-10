(ns indexr.search
  (:import
    [org.apache.lucene.analysis.standard StandardAnalyzer]
    [org.apache.lucene.document Document]
    [org.apache.lucene.index DirectoryReader]
    [org.apache.lucene.queryparser.classic QueryParser]
    [org.apache.lucene.search IndexSearcher Query ScoreDoc]
    [org.apache.lucene.store Directory FSDirectory])
  (:gen-class))

(defn index-search [index-path fieldname criteria]
  (let [reader (DirectoryReader/open (FSDirectory/open index-path))
        analyzer (StandardAnalyzer.)
        searcher (IndexSearcher. reader)
        parser (QueryParser. fieldname analyzer)
        query (.parse parser criteria)]
    (.search searcher query 10)))

;;;;;;;
(require '[clojure.java.io :as io])
(def idx-path (.toPath (io/file "/Users/carlos/idx")))

(def results (index-search idx-path "contents" "darcy"))
(.totalHits results)
