
### source-code-statistics.api

Functional documentation of the source-code-statistics.api Clojure namespace

---

##### [README](../../../README.md) > [DOCUMENTATION](../../COVER.md) > source-code-statistics.api

### Index

- [get-statistics](#get-statistics)

---

### get-statistics

```
@description
Returns file and source code statistics of the given paths.
```

```
@param (strings in vector) paths
@param (map)(opt) options
{:exclude-pattern (regex pattern)(opt)
 :include-pattern (regex pattern)(opt)}
```

```
@usage
(get-statistics ["source-code"])
```

```
@example
(get-statistics ["source-code"])
=>
{:paths [{:path "source-code"
          :files [{:filepath "my-namespace.clj"
                   :stat {:char-count   3214
                          :def-count       0
                          :defn-count     12
                          :new-line-count 18
                          :ns-count        1}}]}]
 :total {:char-count   3214
         :def-count       0
         :defn-count     12
         :file-count      1
         :new-line-count 18
         :ns-count        1}}
```

```
@return (map)
{:paths (maps in vector)
  [{:path (string)
    :files (maps in vector)
     [{:filepath (string)
       :stat (map)
        {:char-count (integer)
         :def-count (integer)
         :defn-count (integer)
         :newline-count (integer)
         :ns-count (integer)}}]}]
 :total (map)
  {:char-count (integer)
   :def-count (integer)
   :defn-count (integer)
   :file-count (integer)
   :newline-count (integer)
   :ns-count (integer)}}
```

<details>
<summary>Source code</summary>

```
(defn get-statistics
  ([paths]
   (get-statistics paths {}))

  ([paths options]
   (letfn [(summarize-path-f [total {:keys [files] :as path-stat}]
                             (-> total (update :file-count    math/add (count files))
                                       (update :char-count    math/add (vector/sum-items-by files #(-> % :stat :char-count)))
                                       (update :def-count     math/add (vector/sum-items-by files #(-> % :stat :def-count)))
                                       (update :defn-count    math/add (vector/sum-items-by files #(-> % :stat :defn-count)))
                                       (update :newline-count math/add (vector/sum-items-by files #(-> % :stat :newline-count)))
                                       (update :ns-count      math/add (vector/sum-items-by files #(-> % :stat :ns-count)))))
           (add-total-f     [paths-stat] {:paths paths-stat :total (reduce summarize-path-f {} paths-stat)})
           (get-file-list-f [path]       {:path path :files (-> path (read/get-file-list options))})
           (read-files-f    [path-stat]  (-> path-stat (update :files vector/->items read-file-f)))
           (read-file-f     [filepath]   (-> filepath io/filepath->extension (case "clj"  {:filepath filepath :stat (-> filepath io/read-file (read/analyse-source-code  options))}
                                                                                   "cljs" {:filepath filepath :stat (-> filepath io/read-file (read/analyse-source-code  options))}
                                                                                   "cljc" {:filepath filepath :stat (-> filepath io/read-file (read/analyse-source-code  options))}
                                                                                          {:filepath filepath :stat (-> filepath io/read-file (read/analyse-file-content options))})))]
          (-> paths (vector/->items get-file-list-f)
                    (vector/->items read-files-f)
                    (add-total-f)))))
```

</details>

<details>
<summary>Require</summary>

```
(ns my-namespace (:require [source-code-statistics.api :refer [get-statistics]]))

(source-code-statistics.api/get-statistics ...)
(get-statistics                            ...)
```

</details>

---

<sub>This documentation is generated with the [clj-docs-generator](https://github.com/bithandshake/clj-docs-generator) engine.</sub>

