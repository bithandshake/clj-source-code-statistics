
(ns source-code-statistics.read
    (:require [io.api     :as io]
              [regex.api  :refer [re-count re-match? re-mismatch?]]
              [vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-file-list
  ; @ignore
  ;
  ; @param (string) path
  ; @param (map) options
  ; {:exclude-pattern (regex pattern)(opt)
  ;  :include-pattern (regex pattern)(opt)}
  ;
  ; @return (strings in vector)
  [path {:keys [exclude-pattern include-pattern]}]
  (letfn [(exclude-f [%] (if exclude-pattern (re-match?    % exclude-pattern)))
          (include-f [%] (if include-pattern (re-mismatch? % include-pattern)))]
         (-> path (io/all-file-list)
                  (vector/remove-items-by exclude-f)
                  (vector/remove-items-by include-f))))

(defn analyse-source-code
  ; @ignore
  ;
  ; @param (string) source-code
  ; @param (map) options
  ;
  ; @return (map)
  ; {:char-count (integer)
  ;  :def-count (integer)
  ;  :defn-count (integer)
  ;  :newline-count (integer)
  ;  :ns-count (integer)}
  [source-code _]
  {:char-count    (count    source-code)
   :def-count     (re-count source-code #"\(def(?![n])")
   :defn-count    (re-count source-code #"\(defn")
   :newline-count (re-count source-code #"[\n\r]")
   :ns-count      (re-count source-code #"\(ns[\n\r\s]{1,}")})

(defn analyse-file-content
  ; @ignore
  ;
  ; @param (string) file-content
  ; @param (map) options
  ;
  ; @return (map)
  ; {:char-count (integer)
  ;  :newline-count (integer)}
  [file-content _]
  {:char-count    (count    file-content)
   :newline-count (re-count file-content #"\n\r")})
