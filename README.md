
# clj-source-code-statistics

### Overview

The <strong>clj-source-code-statistics</strong> is a set of simple source code statistics functions for Clojure projects.

### deps.edn

```
{:deps {monotech-tools/clj-source-code-statistics {:git/url "https://github.com/monotech-tools/clj-source-code-statistics"
                                                   :sha     "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"}}
```

### Current version

Check out the latest commit on the [release branch](https://github.com/monotech-tools/clj-source-code-statistics/tree/release).

### Documentation

The <strong>clj-source-code-statistics</strong> functional documentation is [available here](documentation/COVER.md).

### Changelog

You can track the changes of the <strong>clj-source-code-statistics</strong> library [here](CHANGES.md).

# Usage

> Some parameters of the following functions and some further functions are not discussed in this file.
  To learn more about the available functionality, check out the [functional documentation](documentation/COVER.md)!

### Index

- [How to get statistics of source files?](#how-to-get-statistics-of-source-code-files)

### How to get statistics of source files?

The [`source-code-statistics.api/get-statistics`](documentation/clj/source-code-statistics/API.md#get-statistics)
function returns file and source code statistics of the given paths.

```
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
