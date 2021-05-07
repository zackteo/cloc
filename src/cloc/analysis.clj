(ns cloc.analysis
  (:require [cloc.syntax :as syntax]
            [cloc.util :as util]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn directory-path->file-paths [directory-path]
  (->> (io/file directory-path)
       file-seq
       (remove #(.isDirectory %))
       (map #(.getAbsolutePath %))))

(defn get-relevant-file-paths [lang file-paths]
  (filter #(util/multi-ends-with? % (lang syntax/file-types))
          file-paths))

(defn file-content->num-total-lines [content]
  (count (str/split-lines content)))

(defn file-content->num-blank-lines [content]
  (->> (str/split-lines content)
       (map str/trim) ; empty lines with spaces
       (filter #(= "" %))
       count))

(defn calc-lines-of-code [total-lines comments blanks]
  (- total-lines comments blanks))

(defn partition-multi-line-comment [lang file-content]
  (partition-by
   (let [multi-comment? (atom false)]
     (fn [line]
       (let [region-start? (str/starts-with? line
                                             (lang syntax/multi-line-comment-begin))
             region-end? (str/ends-with? line
                                         (lang syntax/multi-line-comment-end))]
         (cond
            ;; edge case single line  /* comment */
           (and region-start? region-end?) true
           region-start? (do
                           (reset! multi-comment? true)
                           true)
           region-end? (do (reset! multi-comment? false)
                           true)       ; include end delimiter in partition
           :else @multi-comment?))))
   file-content))

(defn file-content->num-comment-lines [lang content]
  (let [multi-line-group (->> (str/split-lines content)
                              (partition-multi-line-comment lang)
                              (group-by #(str/starts-with? (first %)
                                                           (lang syntax/multi-line-comment-begin))))
        lines-single-comment (->> (get multi-line-group false)
                                  flatten
                                  (map str/trim) ; spaces before single line comments
                                  (filter #(str/starts-with? % (lang syntax/single-line-comment)))
                                  count)
        lines-multi-comment (->> (get multi-line-group true)
                                 flatten
                                 (map str/trim) ; empty lines with spaces
                                 (remove #(= "" %))
                                 count)]
    (+ lines-multi-comment lines-single-comment)))

(defn analyse-project [lang project-directory-path]
  (let [file-paths              (directory-path->file-paths project-directory-path)
        relevant-file-paths     (get-relevant-file-paths lang file-paths)
        file-contents           (map slurp relevant-file-paths)
        [total comments blanks] (->> (map (juxt
                                           file-content->num-total-lines
                                           (partial file-content->num-comment-lines lang)
                                           file-content->num-blank-lines) file-contents)
                                     (#(if (seq %) (apply map + %) [0 0 0])))]
    {:files (count relevant-file-paths)
     :lines total
     :code (calc-lines-of-code total comments blanks)
     :comments comments
     :blanks blanks}))

(comment
  (analyse-project :cpp "/home/zackteo/Documents/Clojure/flybot/cloc/resources/test.cpp")
  (analyse-project :ruby "/home/zackteo/Documents/Clojure/flybot/cloc/resources/test.rb")

  (analyse-project :cpp "/home/zackteo/Documents/Clojure/flybot/cloc/resources/Osiris/")
  (analyse-project :ruby "/home/zackteo/Documents/Clojure/flybot/cloc/resources/Osiris/")

  (analyse-project :cpp "/home/zackteo/Documents/Clojure/flybot/cloc/resources/lib-2/")
  (analyse-project :ruby "/home/zackteo/Documents/Clojure/flybot/cloc/resources/lib-2/"))
