#!/usr/bin/env boot

#tailrecursion.boot.core/version "2.0.0"

(set-env!
  :project 'repl
  :version "0.1.0-SNAPSHOT"
  :dependencies '[[tailrecursion/boot.core "2.0.0"]
                  [tailrecursion/boot.task "2.0.0"]
                  [tailrecursion/hoplon "5.1.0"]
                  [org.clojure/clojurescript "0.0-2156"]]
  :out-path "public"
  :src-paths #{"src"})

(require
  '[tailrecursion.boot.task :refer :all]
  '[tailrecursion.hoplon.boot :refer :all]
  '[pmbauer.boot.task.repl :as repl]
  '[pmbauer.boot.task.cljs :as cljs])

(deftask brepl
  "launch browser repl, default point browser to public/index.html"
  [& [index-file]]
  (comp (cljs/+ :browser)
        (cljs/+brepl (or index-file "public/index.html"))
        (repl/repl)))
