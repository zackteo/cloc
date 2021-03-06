(defproject cloc "0.1.0-SNAPSHOT"
  :description "Code Analysis Tool"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "1.0.206"]]
  :repl-options {:init-ns cloc.core}
  :bin {:name "cloc"
        :bin-path "~/bin"
        :jvm-opts ["-server" "-Dfile.encoding=utf-8" "$JVM_OPTS"]}
  :profiles {:uberjar {:aot :all}
             :dev {:plugins [[lein-binplus "0.6.6"]]}}
  :main cloc.cli)
