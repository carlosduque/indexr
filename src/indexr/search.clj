(ns indexr.search
  (:import
    [org.apache.lucene.analysis.standard StandardAnalyzer]
    [org.apache.lucene.document Document]
    [org.apache.lucene.index DirectoryReader]
    [org.apache.lucene.queryparser.classic QueryParser]
    [org.apache.lucene.search IndexSearcher Query ScoreDoc]
    [org.apache.lucene.store Directory FSDirectory])
  (:gen-class))

(defn doc->map [document]
  {:path (.get document "path")
   :created-at (.get document "created-at")})

(defn search-doc [index-path number]
  (let [reader (DirectoryReader/open (FSDirectory/open index-path))
        searcher (IndexSearcher. reader)]
    (.doc searcher number)))

#_(defn search [idx-path limit fieldname criteria]
  (let [reader   (DirectoryReader/open (FSDirectory/open idx-path))
        searcher (IndexSearcher. reader)
        parser   (QueryParser. fieldname (StandardAnalyzer.))
        query    (.parse parser criteria)
        hits     (.search searcher query (int limit))
        total-hits (count (.scoreDocs hits))]
    (doall
      (for [hit (map (partial aget (.scoreDocs hits)) (range 0 total-hits))]
        {:id (.doc hit)
         :score (.score hit)
         :document (doc->map (search-doc idx-path (.doc hit)))}))))

(defn search [idx-path limit fieldname criteria]
  (let [reader   (DirectoryReader/open (FSDirectory/open idx-path))
        searcher (IndexSearcher. reader)
        parser   (QueryParser. fieldname (StandardAnalyzer.))
        query    (.parse parser criteria)
        hits     (.search searcher query (int limit))
        total-hits (count (.scoreDocs hits))]
    (vec
      (for [hit (map (partial aget (.scoreDocs hits)) (range 0 total-hits))]
        {:id (.doc hit)
         :score (.score hit)
         :document (doc->map (search-doc idx-path (.doc hit)))}))))

;;;;;;;
#_(require '[clojure.java.io :as io])
#_(use 'clojure.pprint)
#_(def idx-path (.toPath (io/file "/Users/carlos/idx")))
#_(def hits (search idx-path 10 "contents" "man"))

