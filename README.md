# repl

Equivalent functionality of 'lein repl' for boot

## Usage

### repl

```
boot repl/repl
boot [repl/repl :init-ns \'clojure.core]
boot [repl/repl :middlewares [resolvable-nrepl-middleware-var]]
boot [repl/repl :headless]
boot [repl/repl :headless :init-ns \'clojure.core]
boot [repl/repl :connect]
boot [repl/repl :connect \"localhost:8081\"]
```

### cljs repl
At present, the `cljs/*` tasks are easier to work with in Clojure than they are at the command line.

```
# launches into a headless phantomjs repl
boot [cljs/+ :phantomjs] repl/repl

# launches a cljs repl that a browser can attach to
boot [cljs/+ :browser] repl/repl

# also instruments an html file so it will attach to browser repl
# on load
boot [cljs/+ :browser] [cljs/+brepl \"public/index.html\"] repl/repl
```

### hoplon browser repl

There are still some hoops to jump through, but it works.

```
# for some reason, the browser repl breaks when hosting from the file system
pushd ./public; python -m SimpleHTTPServer

# in another tab ...
# compile public/index.html
boot hoplon

# have not yet determined what triggers copying the index.html output
# file to ./public  For now, two sepparate steps instead of chaining via
# boot hoplon brepl

boot brepl

# after repl loads, refresh http://localhost:8000/index.html

# now in the repl you can do things like
cljs.user=> (ns tailrecursion.hoplon.app_pages._index_DOT_html)
cljs.user=> (reset! person-name "michael")

# ... and the hello message will change (note the gensym means this ns is different each time)
```

## License

Copyright © 2014 Paul Bauer

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
