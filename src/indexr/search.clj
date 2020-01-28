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
#_(require '[clojure.java.io :as io])
#_(def idx-path (.toPath (io/file "/Users/carlos/idx")))
#_(def idx-big (.toPath (io/file "/Users/carlos/index-big")))
#_(def idx-small (.toPath (io/file "/Users/carlos/index-small")))

#_(def results (index-search idx-path "contents" "fish"))
#_(def results (index-search idx-path "k4" "eagle"))
#_(def results-big (index-search idx-big "contents" "fish"))
#_(def results-small (index-search idx-small "contents" "fish"))
#_(map #(.totalHits %) [results results-big results-small])
#_(.totalHits results)
