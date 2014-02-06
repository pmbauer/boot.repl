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
