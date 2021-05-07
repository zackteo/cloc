(ns cloc.util
  (:require [clojure.string :as str]))

(defn multi-ends-with?
  "Similar to str/ends-withs? but for multiple possible substrs"
  [s substrs]
  ((apply some-fn
          (map (fn [substr] #(str/ends-with? % substr))
               substrs))
   s))
