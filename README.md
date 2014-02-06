# repl

Equivalent functionality of 'lein repl' for boot

## Usage

```
boot pmbauer/repl
boot [pmbauer/repl :init-ns \'clojure.core]
boot [pmbauer/repl :middlewares [resolvable-nrepl-middleware-var]]
boot [pmbauer/repl :headless]
boot [pmbauer/repl :headless :init-ns \'clojure.core]
boot [pmbauer/repl :connect]
boot [pmbauer/repl :connect \"localhost:8081\"]
```

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
