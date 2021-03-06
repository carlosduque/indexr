(defproject indexr "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/tools.cli "0.4.2"]
                 [org.apache.lucene/lucene-core "8.4.0"]
                 [org.apache.lucene/lucene-queryparser "8.4.0"]
                 [org.apache.tika/tika-parsers "1.23"]]
  :main ^:skip-aot indexr.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
