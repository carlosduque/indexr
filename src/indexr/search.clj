(ns indexr.search
  (:import
    [org.apache.lucene.analysis.standard StandardAnalyzer]
    [org.apache.lucene.document Document]
    [org.apache.lucene.index DirectoryReader]
    [org.apache.lucene.queryparser.classic QueryParser]
    [org.apache.lucene.search IndexSearcher Query ScoreDoc]
    [org.apache.lucene.store Directory FSDirectory])
  (:gen-class))

#_(defn index-search [index-path limit fieldname criteria]
  (let [reader (DirectoryReader/open (FSDirectory/open index-path))
        analyzer (StandardAnalyzer.)
        searcher (IndexSearcher. reader)
        parser (QueryParser. fieldname analyzer)
        query (.parse parser criteria)]
    (.search searcher query limit)))

(defn index-search-doc [index-path number]
  (let [reader (DirectoryReader/open (FSDirectory/open index-path))
        searcher (IndexSearcher. reader)]
    (.doc searcher number)))

(defn index-search [index-path limit fieldname criteria]
  (let [reader   (DirectoryReader/open (FSDirectory/open index-path))
        analyzer (StandardAnalyzer.)
        searcher (IndexSearcher. reader)
        parser   (QueryParser. fieldname analyzer)
        query    (.parse parser criteria)
        hits     (.search searcher query (int limit))
        total    (count (.scoreDocs hits))]
    (doall
      (for [hit (map (partial aget (.scoreDocs hits)) (range 0 total))]
        (index-search doc index-path (.doc hit))))))

;;;;;;;
#_(require '[clojure.java.io :as io])
#_(def idx-path (.toPath (io/file "/home/carlos/idx")))
#_(def hits (index-search idx-path 10 "contents" "aladdin"))
#_(.totalHits hits)

#_(map #(index-search-doc idx-path %) (range 0 (count (.scoreDocs (index-search idx-path "contents" "aladdin")))))

