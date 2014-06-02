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
```

### hoplon browser repl

```
# use brepl example task
boot brepl

# in another terminal ...
boot [repl/repl :connect]

# after repl loads, refresh http://localhost:8000/index.html

# now in the repl you can do things like
cljs.user=> (ns tailrecursion.hoplon.app_pages._index_DOT_html)
cljs.user=> (reset! person-name "michael")

# ... and the hello message will change
# The brepl example task also includes a watch,
# so editing hoplon source files will recompile the target artifacts,
# including the brepl html instrumentation.
# Refresh the browser to get source updates and reconnect to brepl session.
# There is some jankiness, may need to refresh a few times in this case.
```

## License

Copyright © 2014 Paul Bauer

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
