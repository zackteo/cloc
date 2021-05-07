(ns cloc.cli
  (:require [cloc.analysis :refer [analyse-project]]
            [cloc.syntax :as syntax]
            [clojure.java.io :as io]
            [clojure.pprint :as pprint]
            [clojure.string :as str]
            [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(defn usage [options-summary]
  (->> ["Cloc: C++ lines of code"
        ""
        "Usage: cloc [options] file-path"
        ""
        "Options:"
        options-summary
        ""]
       (str/join \newline)))

(def cli-options
  ;; An option with a required argument
  [["-l" "--lang LANG" "Programming Language"
    :parse-fn #(str %)
    :validate [(set syntax/programming-languages)
               (apply str "Must be a programming language from list: "
                      (interpose ", " syntax/programming-languages))]]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)
        lang (keyword (:lang options))
        path (first arguments)]

    (cond
      (true? (:help options))        (println (usage summary))
      (nil? lang)                    (println "Specify a programming language")
      (not (.exists (io/file path))) (println (str "Invalid file path: " path))

      (nil? errors)                  (clojure.pprint/print-table
                                       (vector (analyse-project lang path)))
      :else                          (println (first errors)))))

(comment
  (-main "--lang" "ruby" "/home/zackteo/Documents/Clojure/flybot/cloc/resources/test.rb")

  (-main "--lang" "cpp" "/home/zackteo/Documents/Clojure/flybot/cloc/resources/test.cpp")

  (-main "--lang" "cpp" "/home/zackteo/Documents/Clojure/flybot/cloc/resources/Osiris")

  ;; help
  (-main "--help")

  ;; language not found
  (-main "--lang" "language?" "/home/zackteo/Documents/Clojure/flybot/cloc/resources/Osiris")

  ;; invalid file path
  (-main "--lang" "cpp" "/home/zackteo/Documents/invalid")

  (-main "/home/zackteo/Documents/Clojure/flybot/cloc/resources/Osiris")

  (defn main-test [& args]
    (parse-opts args cli-options))

  (:options (main-test  "--help" "--lang" "ruby" "/resources")))
