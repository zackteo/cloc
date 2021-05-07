(ns cloc.syntax)

(def programming-languages
  ["cpp" "ruby"])

(def file-types
  {:cpp [".cpp" ".c" ".cc"]
   :ruby [".rb"]})

(def multi-line-comment-begin
  {:cpp "/*"
   :ruby "=begin"})

(def multi-line-comment-end
  {:cpp "*/"
   :ruby "=end"})

(def single-line-comment
  {:cpp "//"
   :ruby "#"})
