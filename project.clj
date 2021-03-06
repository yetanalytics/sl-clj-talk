(defproject sl-clj-talk "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.5.3"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.293"]
                 [org.clojure/core.async "0.2.395"
                  :exclusions [org.clojure/tools.reader]]
                 [reagent "0.6.0"]
                 [com.cognitect/transit-cljs "0.8.239"]
                 [replumb "0.2.4"]
                 [cljsjs/highlight "9.6.0-0"]]

  :plugins [[lein-figwheel "0.5.8"]
            [lein-cljsbuild "1.1.3" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src" "src/cljs"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :profiles {:dev {:source-paths ["src" "src/cljs" "dev"]
                   :dependencies [[compojure "1.5.1"]
                                  [http-kit "2.2.0"]
                                  [hiccup "1.0.5"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [figwheel-sidecar "0.5.8"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}}

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src/cljs"]

                ;; If no code is to be run, set :figwheel true for continued automagical reloading
                :figwheel true ;; {:on-jsload "sl-clj-talk.core/on-js-reload"}

                :compiler {:main sl-clj-talk.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/sl_clj_talk.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true}}
               ;; This next build is an compressed minified build for
               ;; production. You can build this with:
               ;; lein cljsbuild once min
               {:id "min"
                :source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/compiled/sl_clj_talk.js"
                           :main sl-clj-talk.core
                           :optimizations :simple
                           :pretty-print false}}]}

  :figwheel {;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this
             ;; doesn't work for you just run your own server :)
             ;; :ring-handler hello_world.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"
             })
