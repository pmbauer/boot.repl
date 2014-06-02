(ns pmbauer.boot.task.cljs
  (:refer-clojure :exclude [+])
  (:require [tailrecursion.boot.core :as boot]
            [clojure.java.io :as io]))

(def ^:dynamic repl-env nil)

(def init-scripts
  {:phantomjs
   (delay
    '(do (require 'cemerick.austin
                  'cemerick.austin.repls)
         (cemerick.austin.repls/cljs-repl (cemerick.austin/exec-env))))

   :browser
   (delay
     (require 'cemerick.austin
              'cemerick.austin.repls)
     (alter-var-root #'repl-env (constantly ((resolve 'cemerick.austin/repl-env))))
     (reset! (deref (resolve 'cemerick.austin.repls/browser-repl-env)) repl-env)
     `(cemerick.austin.repls/cljs-repl repl-env))

   nil
   (delay '(do))})

(defn wrap-init-cljs-repl
  [init-script]
  (with-local-vars
      [wrap-init-cljs-repl'
       (fn [h]
         ;; this needs to be a var, since it's in the nREPL session
         (with-local-vars [init-cljs-repl-sentinel nil]
           (fn [{:keys [session transport code] :as msg}]
             (let [msg (atom msg)]
               (when-not (@session init-cljs-repl-sentinel)
                 (swap! session #(do (swap! msg assoc :code
                                            (str "(do " code (pr-str init-script) ")"))
                                     (assoc % init-cljs-repl-sentinel true))))
               (h @msg)))))]
    (doto wrap-init-cljs-repl'
      ;; set-descriptor! currently nREPL only accepts a var
      ((resolve 'clojure.tools.nrepl.middleware/set-descriptor!)
       {:requires #{(resolve 'clojure.tools.nrepl.middleware.session/session)
                    (resolve 'cemerick.piggieback/wrap-cljs-repl)}
        :expects #{"eval"}})
      (alter-var-root (constantly @wrap-init-cljs-repl')))))

(boot/deftask +
  "instrument task chain with cljs bits"
  [& [cmd]]
  (assert (some (comp #{'org.clojure/clojurescript} first) (boot/get-env :dependencies))
          "A version of org.clojure/clojurescript must be in boot :dependencies")
  (boot/set-env! :dependencies '[[com.cemerick/austin "0.1.4" :exclusions [org.clojure/clojurescript]]])
  (require 'cemerick.piggieback)
  (fn [continue]
    (fn [event]
      (continue (assoc event
                  :pmbauer.boot.task.repl/config
                  {:middlewares [(wrap-init-cljs-repl @(get init-scripts cmd))
                                 (resolve 'cemerick.piggieback/wrap-cljs-repl)]})))))

(boot/deftask +brepl
  "inject browser-repl connect directive into html"
  [& paths]
  (boot/set-env! :dependencies '[[enlive "1.1.5"]])
  (require 'pmbauer.boot.task.cljs.inject)
  (let [path-patterns (map re-pattern (or paths ["index.html"]))
        ;;output-dir (boot/mkoutdir! ::brepl-instrumented)
        ]
    (boot/with-pre-wrap
      (let [inject (resolve 'pmbauer.boot.task.cljs.inject/-inject-brepl)]
        (println "Instrumenting html with browser repl directive ...")
        (doseq [f (->> (boot/out-files) (boot/by-re path-patterns) doall)]
          (println "•" (boot/relative-path f))
          (spit f (inject f)))))))
