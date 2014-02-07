(ns pmbauer.boot.task.cljs
  (:refer-clojure :exclude [+])
  (:require [tailrecursion.boot.core :as core]
            [clojure.java.io :as io]))

(def ^:dynamic repl-env nil)

(def init-scripts
  {:phantomjs
   (constantly
    '(do (require 'cemerick.austin
                  'cemerick.austin.repls)
         (cemerick.austin.repls/cljs-repl (cemerick.austin/exec-env))))

   :browser
   (fn []
     (require 'cemerick.austin
              'cemerick.austin.repls)
     (alter-var-root #'repl-env (constantly ((resolve 'cemerick.austin/repl-env))))
     (reset! (deref (resolve 'cemerick.austin.repls/browser-repl-env)) repl-env)
     `(cemerick.austin.repls/cljs-repl repl-env))

   nil
   (constantly '(do))})

(core/deftask +
  "instrument task chain with cljs bits"
  [& [cmd]]
  ;; todo: configure clojurescript/austin versions ??
  (core/set-env!
   :dependencies '[[org.clojure/clojurescript "0.0-2156"]
                   [com.cemerick/austin "0.1.3"
                    :exclusions [org.clojure/clojurescript]]])
  (require 'cemerick.piggieback)
  (fn [continue]
    (fn [event]
      (continue (assoc event
                  :pmbauer.boot.task.repl/config
                  {:repl-options {:custom-eval ((get init-scripts cmd))}
                   :middlewares [(resolve 'cemerick.piggieback/wrap-cljs-repl)]})))))

(core/deftask +brepl
  "inject browser-repl connect directive into html"
  [& paths]
  (core/set-env! :dependencies '[[enlive "1.1.5"]])
  (require 'pmbauer.boot.task.cljs.inject)
  (core/with-pre-wrap
    (let [inject (resolve 'pmbauer.boot.task.cljs.inject/-inject-brepl)]
      (doseq [filename paths]
        (let [f (io/file filename)]
          (spit f (inject f)))))))
