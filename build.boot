#!/usr/bin/env boot

#tailrecursion.boot.core/version "2.0.0"

(set-env!
  :project 'pmbauer.boot.task/repl
  :version "0.1.0-SNAPSHOT"
  :dependencies '[[tailrecursion/boot.core "2.0.0"]
                  [tailrecursion/boot.task "2.0.0"]]
  :out-path "target"
  :src-paths #{"src"})

(require
  '[tailrecursion.boot.task :refer :all]
  '[pmbauer.boot.task.repl :as pmbauer])

(deftask cljs-play
  "cljs repl playground"
  []
  (set-env! :dependencies '[[org.clojure/clojurescript "0.0-2156"]
                            [ring "1.2.1"]
                            [compojure "1.1.6"]
                            [hiccup "1.0.5"]
                            [com.cemerick/austin "0.1.3"
                             :exclusions [org.clojure/clojurescript]]])
  (require 'cemerick.piggieback)
  (comp (cljs :output-to "target/app.js")
        (pmbauer/repl
         :init-ns 'cljs-repl
         :middlewares [#'cemerick.piggieback/wrap-cljs-repl])))
