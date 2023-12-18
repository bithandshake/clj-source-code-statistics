
(ns source-code-statistics.engine
    (:require [fruits.math.api             :as math]
              [fruits.vector.api           :as vector]
              [io.api                      :as io]
              [source-code-statistics.read :as read]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-statistics
  ; @description
  ; Returns statistics of files and source files found at the given source paths.
  ;
  ; @param (map) options
  ; {:filename-pattern (regex pattern)(opt)
  ;   Default: #"[a-z\d\_]\.clj[cs]{0,1}"
  ;  :source-paths (strings in vector)}
  ;
  ; @usage
  ; (get-statistics {:source-paths ["source-code"]})
  ; =>
  ; {:source-paths [{:source-path "source-code"
  ;                  :files [{:filepath "source-code/my_namespace.clj"
  ;                           :stat {:char-count   3214
  ;                                  :def-count       0
  ;                                  :defn-count     12
  ;                                  :new-line-count 18
  ;                                  :ns-count        1}}]}]
  ;  :total {:char-count   3214
  ;          :def-count       0
  ;          :defn-count     12
  ;          :file-count      1
  ;          :new-line-count 18
  ;          :ns-count        1}}
  ;
  ; @return (map)
  ; {:source-paths (maps in vector)
  ;   [{:source-path (string)
  ;     :files (maps in vector)
  ;      [{:filepath (string)
  ;        :stat (map)
  ;         {:char-count (integer)
  ;          :def-count (integer)
  ;          :defn-count (integer)
  ;          :newline-count (integer)
  ;          :ns-count (integer)}}]}]
  ;  :total (map)
  ;   {:char-count (integer)
  ;    :def-count (integer)
  ;    :defn-count (integer)
  ;    :file-count (integer)
  ;    :newline-count (integer)
  ;    :ns-count (integer)}}
  [{:keys [source-paths] :as options}]
  (letfn [(summarize-path-f [total {:keys [files] :as source-path-stat}]
                            (-> total (update :file-count    math/add (count files))
                                      (update :char-count    math/add (vector/sum-items-by files #(-> % :stat :char-count)))
                                      (update :def-count     math/add (vector/sum-items-by files #(-> % :stat :def-count)))
                                      (update :defn-count    math/add (vector/sum-items-by files #(-> % :stat :defn-count)))
                                      (update :newline-count math/add (vector/sum-items-by files #(-> % :stat :newline-count)))
                                      (update :ns-count      math/add (vector/sum-items-by files #(-> % :stat :ns-count)))))
          (add-total-f     [source-paths-stat] {:source-paths source-paths-stat :total (reduce summarize-path-f {} source-paths-stat)})
          (get-file-list-f [source-path]       {:source-path source-path :files (-> source-path (read/get-file-list options))})
          (read-files-f    [source-path-stat]  (-> source-path-stat (update :files vector/->items read-file-f)))
          (read-file-f     [filepath]          (-> filepath io/filepath->extension (case "clj"  {:filepath filepath :stat (-> filepath io/read-file (read/analyse-source-code  options))}
                                                                                         "cljs" {:filepath filepath :stat (-> filepath io/read-file (read/analyse-source-code  options))}
                                                                                         "cljc" {:filepath filepath :stat (-> filepath io/read-file (read/analyse-source-code  options))}
                                                                                                {:filepath filepath :stat (-> filepath io/read-file (read/analyse-file-content options))})))]
         (-> source-paths (vector/->items get-file-list-f)
                          (vector/->items read-files-f)
                          (add-total-f))))
