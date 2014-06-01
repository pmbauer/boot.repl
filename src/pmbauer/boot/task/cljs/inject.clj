(ns pmbauer.boot.task.cljs.inject
  (:require [net.cgrand.enlive-html :refer :all]
            [cemerick.austin.repls :refer (browser-connected-repl-js)]))

(defn -injection [source]
  (let [header-script-path [:head [:script last-of-type]]
        header-script? (not (empty? (select (html-resource source) header-script-target)))]
    (if header-script?
      {:path header-script-path
       :script (browser-connected-repl-js)}
      {:path [:body]
       :script (html [:script {:type "text/javascript"}
                      (browser-connected-repl-js)])})))

(defn -inject-brepl [source]
  (letfn [(insert-after [path s] (template source [] path (append s)))]
    (let [{:keys [path script]} (-injection source)]
      (apply str ((insert-after path script))))))
