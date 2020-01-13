(ns indexr.app
  (:import
    (org.apache.lucene.search IndexSearcher Query))
  (:gen-class))

(defn run [opt arg]
  (let [idx (:index opt)
        file (:file opt)]
    (println (str "idx: " idx " \nfile: " file))))


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

