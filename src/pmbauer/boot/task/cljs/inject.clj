(ns pmbauer.boot.task.cljs.inject
  (:require [net.cgrand.enlive-html :as enlive]
            [cemerick.austin.repls :refer (browser-connected-repl-js)]))

(defn -inject-brepl [source]
  (let [t (enlive/template source [] [:body]
            (enlive/append (enlive/html [:script (browser-connected-repl-js)])))]
    (apply str (t))))
