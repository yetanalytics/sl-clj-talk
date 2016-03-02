(ns sl-clj-talk.slides
  (:require
   [reagent.core :as reagent :refer [atom]]
   [clojure.string :as cstring]
   [cljs.pprint :refer [pprint]]))



(defn highlight! [component]
  (.highlightBlock js/hljs
                   (reagent/dom-node component)))

(defn wrap-highlight [component]
  (reagent/create-class
   {:reagent-render component
    :component-did-mount highlight!
    :component-did-update highlight!}))

(defn code-block* [code-type code-str]
  [:code
   {:class (str "hljs "
                code-type)}
   code-str])


(def code-block
  (wrap-highlight
   code-block*))


(defn js-example [indent-n & ss]
  [:pre
   [code-block
    "javascript"
    (str
     ;; "\n"
     (cstring/join "\n"
                  (map (partial str
                                (apply str
                                       (repeat indent-n " ")))
                       ;; handle existing line breaks
                       (flatten (map cstring/split-lines ss))))
     "\n" ;; "\n\n"
     )]])

(defn clj-example [indent-n & ss]
  [:pre
   [code-block
    "clojure"
    (str
     ;; "\n"
     (cstring/join "\n"
                   (map (partial str
                                 (apply str
                                        (repeat indent-n " ")))
                        (flatten (map cstring/split-lines ss))))
     "\n" ;; "\n\n"
     )]])

(defn clj-repl-example [indent-n & ss]
  [:pre
   [code-block
    "clojure"
    (str
     ;; "\n"
     (cstring/join "\n"
                   (map (partial str
                                 (apply str
                                        (repeat indent-n " ")))
                        (flatten (map cstring/split-lines ss))))
     ;; "\n\n"
     "\n")]])

(defn clj-data-example [indent-n & data]
  [:pre
   [code-block
    "clojure"
    (str
     "\n"
     (cstring/join "\n"
                   (map (partial str
                                 (apply str
                                        (repeat indent-n " ")))
                        data))
     "\n\n")]])


(defn fragment-list [tag & items]
  (into
   [tag]
   (for [item items]
     [:li.fragment item])))

(def title-slide
  [:section
   [:h1 "'Getting' Clojure"]
   [:h3 "'(Parentheses are just hugs for your code)"]
   [:small
    "Original Created by\n            "
    [:a {:href "http://twitter.com/canweriotnow"} "Jason Lewis"]
    "\n            / "
    [:a {:href "http://twitter.com/gtrakgt"} "Gary Trakhman"]]
   [:br]
   [:small
    "Cljs Version by\n"
    [:a {:href "http://twitter.com/miltreder"} "Milt Reder"]]])

(defn js-2-clojure []
  [:section
   [:div
    [:h3 "Functions"]
    [js-example
     30
     "function(){"
     "  return 5;"
     "  }"
     ]]
   [:div.fragment
    "Put some parens around it, kill the braces"

    [js-example
     30
     "(function()"
     "  return 5;"
     "  )"
     ]]

   [:div.fragment
    "Change 'function' to 'fn', makes args into a vector"
    [clj-example
     30
     "(fn []"
     "  return 5;"
     "  )"]]

   [:div.fragment
    "Kill the 'return', last thing's always returned."
    [clj-example
     30
     "(fn [] 5)"]

    "Welcome to Clojure."
    [:div.fragment
     [:small
      "Everything is an expression (just like Ruby!)"]]]])


(defn calling-stuff []
  [:section
   [:h3 "Calling Stuff"]
   [js-example
    20
    "someFunction(arg1, arg2, arg3)"]
   [:div.fragment
    "Move the left parenthesis over a bit more..."
    [clj-example
     20
     "(someFunction arg1 arg2 arg3)"]
    "Done."]])


(defn data-intro []
  [:section
   [:h2 "Data"]
   "Should look familiar"
   [clj-data-example
    25
    {:key1 5 :key2 nil}
    [1 2 3 4 "five"]]

   [:p.fragment "Don't freak out"]

   [:div.fragment
    [clj-data-example
     20
     '[1 [2] #{3} {4 4} (constantly 5)]]]

   [:p.fragment "DON'T FREAK OUT"]

   [:div.fragment
    [clj-repl-example
     0
     "=> (range 10)"
     "(0 1 2 3 4 5 6 7 8 9)"
     "=> (take 11 (range))"
     "(0 1 2 3 4 5 6 7 8 9 10)"
     "=> (last (range)) ;; Hope you don't mind waiting a long time."]]])

(defn everything-is-data []
  [:section
   [:h2 "Everything is Data"]
   [clj-example
    0
    ";; semicolons are comments, commas are ignored,"
    ";; check out this weird hash-map"
    "{:a-keyword 5,"
    " \"a string key\" \"a string value\","
    " [\"a\" :vector \"acting\" :as [:a :compound] \"key\"]"
    " (fn [] \"a no-arg function\n  that returns this multi-line string,\n  the function itself is the value\"),"
    " + '(functions can be keys too, and when\n    you quote symbols, you just\n    have symbols, not what they represent)}"]

   [:div.fragment
    "Evals to.."
    [clj-example
     0
     "{:a-keyword 5,"
     " \"a string key\" \"a string value\","
     " [\"a\" :vector \"acting\" :as [:a :compound] \"key\"]"
     " #&ltuser$eval331$fn__332 user$eval331$fn__332@a585ef>,"
     " #&ltcore$_PLUS_ clojure.core$_PLUS_@20a12d8f>"
     " (functions can be keys too and when you quote symbols\n you just have symbols not what they represent)}"]]])


(defn immutability-intro []
  [:section
   [:h3 "Anything can be a key, because"]

   ;; TODO split or replace
   [fragment-list
    :ol
    [:div
     "Every object is also a 'value'"
     [clj-example 0
      ";; even functions"
      "(defn hello-world [] \"Hello, world!\")"
      ";; #'user/hello-world"
      ";; same as (def (fn [] ...))"]]
    "Values have true equality"
    "Values Never Change (Immutability)"
    "Without immutability, objects are just buckets in memory"]
   [:small.pad80.fragment
    "...have you ever trusted a bucket with no values?"]])

(defn immutability-questions []
  [:section
   "Some Questions..."
   [fragment-list :ul
    "Q: Why should I care about (immutable) values?"
    "A: I can write code and rest assured that other parts of my program can't change the data that I'm working on."
    "Q: But I thought every program is simply a short-lived http request handler that talks to a database? We just throw the program state out after every request!"
    "A: Well, that's one way to do it."]])




(defn misc-examples []
  [:section
   [:section
    [:h1 "Code Examples"]]
   [:section
    "\n          What if, as a library author, you could just not write that\n          fluent interface code at all?\n          "
    [clj-example 0
     "\n(use 'clojure.string)\n\n;; These are equivalent\n\n(map trim (split (upper-case \"hola, world\") #\",\"))\n;; (\"HOLA\" \"WORLD\")\n\n(-> \"hola, world\"\n    upper-case\n    (split #\",\")\n    (->> (map trim)))\n;; (\"HOLA\" \"WORLD\")\n          "]]

   [:section
    "Really useful when you're doing a lot of collection operations, filtering, etc."
    [clj-example 0
     "(->> (range)\n     (filter even?)\n     (map (partial * 2))\n     (take 10)\n     (into []))\n;; [0 4 8 12 16 20 24 28 32 36]\n\n;; versus\n(into []\n      (take 10 (map (partial * 2)\n                    (filter even? (range)))))\n          "]
    [fragment-list :ol
     "I find the flat one easier to think about."
     "Semantically equivalent."
     "No burden on implementing code. Functions don't care about how they're used."]
    [:p.fragment
     "\n          Giving the user choices is more effective with more powerful languages. Leads to simple, composable libraries."]]

   [:section
    [:h3 "Macros"]
    "Let's look at a real one."
    [clj-example 0
     "(defmacro lazy-seq"
     "  \"Takes a body of expressions that returns an ISeq or nil,"
     "  and yields a Seqable object that will invoke the body only"
     "  the first time seq is called, and will cache the result and"
     "  return it on all subsequent seq calls. See also - realized?\""
     "  {:added \"1.0\"}"
     "  [& body]"
     "  (list 'new 'clojure.lang.LazySeq"
     "        (list* '^{:once true} fn* [] body)))"
     "  ;; simply returns a list, allocates a Java object (LazySeq)"
     "  ;; and-wraps your expressions in a function"
     "(macroexpand-1 '(lazy-seq ANYTHING1 ANYTHING2))"
     ";; => '(new clojure.lang.LazySeq (fn* [] ANYTHING1 ANYTHING2))"]]

   [:section
    "Let's create an infinite sequence representing a square-wave"
    [:br]
    "\n          --__--__--__--__\n\n          "
    [clj-example 0
     "(defn square-wave\n  \"t is the period for a half-cycle\"\n  [t]\n  (letfn\n    [(osc [cur-value so-far]\n       (let [so-far (mod so-far t)\n             next-val (if (zero? so-far)\n                        (- cur-value)\n                        cur-value)]\n         (cons next-val\n               (lazy-seq (osc next-val\n                              (inc so-far))))))]\n    (osc 1 0)))\n          "]
    [clj-example 0
     "(take 10 (square-wave 3))"
     ";; (-1 -1 -1 1 1 1 -1 -1 -1 1)"]
    "\n          No mutable variables\n        "]])


(defn slides-div []
  [:div.slides
   title-slide

   ;; function basics
   [js-2-clojure]
   [calling-stuff]

   ;; smug lisp weenieism
   [:section
    [:h2 "This isn't an accident"]
    [:ul
     [:li.fragment "Javascript is 'Lisp in C's Clothing'"]
     [:li.fragment
      "Says Crockford: "
      [:a
       {:href "http://www.crockford.com/javascript/javascript.html"}
       "http://www.crockford.com/javascript/javascript.html"]]]]

   [:section
    [:h2 "Put another way..."]
    [:ul
     [:li
      "Q: Why do you think we've gotten so much mileage out of javascript?"]
     [:li.fragment "A: Lisp is very powerful, and it will never die"]]]

   ;; data
   [data-intro]
   [everything-is-data]


   ;; immutability
   [immutability-intro]
   [immutability-questions]

   [:section
    [:h2 "What We Really Want"]
    "Tools that let us"
    [fragment-list :ol
     "Compose Systems"
     "Change our minds"
     "Re-use components in different contexts, processes, servers, etc.."]
    [:small.pad80.fragment
     "Data/Values give us the ability to decouple things easily"]]

   [:section
    [:h1 "R-E-P-L"]
    "Read-Eval-Print-Loop"
    [fragment-list :ol
     "Read: (read-string \"(+ 1 2)\") => '(+ 1 2)"
     "Eval: (eval '(+ 1 2)) => 3"
     "What if there's something in the middle?"]
    [:div.fragment
     [clj-example
      0
      "\n(class (read-string \"(+ 1 2)\"))\n;; clojure.lang.PersistentList\n\n(map class (read-string \"(+ 1 2)\"))\n;; (clojure.lang.Symbol java.lang.Long java.lang.Long)\n          "]]]


   ;; TODO.. just a rando example. move or remove?
   [:section
    [clj-example 10
     "\n(defn only-even!\n [val]\n (if (and (integer? val) (odd? val))\n   (inc val)\n   val))\n\n(map only-even! (read-string \"(+ 1 2)\"))\n;; '(+ 2 2)\n\n(eval (map only-even! (read-string \"(+ 1 2)\")))\n;; 4\n          "]
    "\n\nThis is only the beginning\n        "]







   [:section
    [:img {:src "lib/img/fig17.gif"}]
    [:small
     [:a
      {:href
       "http://www.ibm.com/developerworks/library/wa-aj-multitier2/"}
      "http://www.ibm.com/developerworks/library/wa-aj-multitier2/"]]]


   [misc-examples]

   [:section
    [:h3 "Resources"]
    [:div
     "Clojure: "
     [:a {:href "http://clojure.org"} "http://clojure.org"]]
    [:div
     "Fun Exercises: "
     [:a
      {:href "http://www.4clojure.com"}
      "http://www.4clojure.com"]]
    [:div
     "Cheatsheets: "
     [:a
      {:href "http://clojure.org/cheatsheet"}
      "http://clojure.org/cheatsheet"]]
    [:div
     "Building: "
     [:a
      {:href "https://github.com/technomancy/leiningen"}
      "https://github.com/technomancy/leiningen"]]
    [:div
     "Insight: "
     [:a
      {:href "http://www.youtube.com/user/ClojureTV"}
      "http://www.youtube.com/user/ClojureTV"]]
    [:div
     "Community docs: "
     [:a {:href "http://clojuredocs.org"} "http://clojuredocs.org"]]
    [:div
     "Blogs: "
     [:a
      {:href "http://planet.clojure.in"}
      "http://planet.clojure.in"]]
    [:div
     "Light Table: "
     [:a
      {:href "http://www.lighttable.com"}
      "http://www.lighttable.com"]]
    [:div
     "this doc: "
     [:a
      {:href "http://gtrak.github.io/bohconf.clojure"}
      "http://gtrak.github.io/bohconf.clojure"]]]
   [:section [:h3 "MORE Demo Time"]]
   [:section
    [:h3 "Thanks for coming!"]
    [:h5 "We are:"]
    [:hr]
    [:div
     [:p "Gary Trakhman"]
     [:p [:a {:href "https://twitter.com/gtrakGT"} "@gtrakGT"]]
     [:p
      "\n              Software Engineer at "
      [:a {:href "http://www.revelytix.com/"} "Revelytix, Inc."]]]
    [:hr]
    [:div
     [:p "Jason Lewis"]
     [:p
      [:a
       {:href "https://twitter.com/canweriotnow"}
       "@canweriotnow"]]
     [:p
      "\n              CTO at "
      [:a {:href "http://anestuary.com"} "An Estuary, LLC"]]]
    [:hr]]])
