(ns indexr.index
  (:require
    [clojure.java.io :as io])
  (:import
    [org.apache.lucene.analysis Analyzer]
    [org.apache.lucene.analysis.standard StandardAnalyzer]
    [org.apache.lucene.document Document Field FieldType Field$Store StringField TextField]
    [org.apache.lucene.index IndexWriter IndexWriterConfig IndexWriterConfig$OpenMode
                             DirectoryReader IndexOptions]
    [org.apache.lucene.queryparser.classic QueryParser]
    [org.apache.lucene.search IndexSearcher Query ScoreDoc]
    [org.apache.lucene.store Directory FSDirectory]
    [org.apache.tika.exception TikaException]
    [org.apache.tika.metadata Metadata]
    [org.apache.tika.parser AutoDetectParser ParseContext Parser]
    [org.apache.tika.sax BodyContentHandler]
    [org.xml.sax ContentHandler]
    [org.xml.sax SAXException])
  (:gen-class))

(defn to-str [value]
  (if (keyword? value)
    (name value)
    (str value)))

(defn add-field [document k v]
  (let [field-type (doto (FieldType.) (.setTokenized true) (.setIndexOptions IndexOptions/DOCS))]
    (if (= k :contents)
      (.add document (Field. (to-str k) v field-type))
      (.add document (StringField. (to-str k) (to-str v) Field$Store/YES)))))

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

(defn extract-contents [file]
  (let [metadata (Metadata.)
        handler  (BodyContentHandler.)
        context  (ParseContext.)
        parser   (AutoDetectParser.)]
    (with-open [stream (io/input-stream file)]
      (try
        (.parse parser stream handler metadata context)
        (catch TikaException e
          (println "tika exception:" (.getMessage e)))
        (catch Exception e
          (println "general exception:" (.getMessage e)))))
    (.toString handler)))

(defn index-file [fields writer]
  (.addDocument writer
                (fields->doc (assoc fields :contents
                                    (extract-contents (:file fields))))))

