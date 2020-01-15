(ns indexr.app
  (:require
    [clojure.java.io :as io])
  (:import
    [org.apache.lucene.analysis Analyzer]
    [org.apache.lucene.analysis.standard StandardAnalyzer]
    [org.apache.lucene.document Document Field Field$Store StringField TextField]
    [org.apache.lucene.index IndexWriter IndexWriterConfig]
    [org.apache.lucene.search IndexSearcher Query]
    [org.apache.lucene.store Directory FSDirectory])
  (:gen-class))

(defn add-field [document field-key field-value]
  (.add document
        (TextField. field-key field-value)))

(defn add [writer file]
  (let [document (Document.)
        ms (System/currentTimeMillis)
        reader (io/reader file)]
    (with-open [reader (io/reader file)]
      (add-field document "contents" reader))))

(defn run [opt]
  (let [idx-dir (FSDirectory/open (:index opt))
        analyzer (StandardAnalyzer.)
        idx-cfg (IndexWriterConfig. analyzer)
        idx-writer (IndexWriter. idx-dir idx-cfg)
        file (:file opt)]
    (with-open [idx-writer (IndexWriter. idx-dir idx-cfg)]
      (add idx-writer file))))


;(defn create-document [fields]
;  (let [doc (Document.)]
;    ))

;;Field$Index/ANALYZED

;;try {
;;     String idxPath = "some/path";
;;     Directory dir = FSDirectory.open(idxPath);
;;     Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
;;     IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_46, analyzer);
;;     IndexWriter writer = new IndexWriter(dir, iwc);
;;     //...add docs
;;     String text = "thi is the text to be indexed";
;;     Document doc = new Document();
;;     doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
;;     writer.addDocument(doc);
;;     //...add docs
;;     writer.close();
;;     } except (IOException e) {
;;       //errorr
;;     }
;;}


;;DirectoryReader reader = DirectoryReader.open(directory);
;;IndexSearcher searcher = new IndexSearcher(reader);
;;QueryParser parser = new QueryParser("fieldname", analyzer);
;;Query query = parser.parse("text");
;;ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;
;;for (int i=0; i < hits.length; i++) {
;;         Document hitDoc = searcher.doc(hits[i].doc);
;;         Sys.out.print(hitDoc.get("fieldname"));
;;}
;;reader.close();
;;directory.close();

