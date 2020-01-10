(ns indexr.core
  (:gen-class)
  (:require
    [indexr.app :as app]
    [clojure.string :as string]
    [clojure.java.io :as io]
    [clojure.tools.cli :as cli]))

(defn- usage [options-summary]
  (->> ["This is Oraqus' IndexR, use it to index your books"
        ""
        "Usage: program-name -i /path/to/index -q someTerm"
        "Options:"
        options-summary]
        (string/join \newline)))

(defn- error-msg [errors]
  (str "something went wrong while processing the options" (string/join \newline errors)))

(defn- exit [status msg]
  (println msg)
  (System/exit status))

(def cli-options
  [["-h" "--help" ]
   ["-i" "--index INDEX" "Path to index"
    :default ".index/"
    ;:parse-fn #(somefn %)
    :validate [#((.isDirectory (io/file %))) "must be a directory"]]

   ["-q" "--query PATTERN" "The search query"
    :default "*"
    ;:parse-fn #()
    ]])

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options) (exit 0 (usage summary))
      errors (exit 1 (error-msg errors))
      :else
      (try
           (app/run options)
           (catch Exception e
             (.printStackTrace e)
             (println "Danger Will Robinson! " (.getMessage e))
             (exit 1 (usage summary)))))))

