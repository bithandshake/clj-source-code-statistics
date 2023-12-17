
(ns source-code-statistics.read
    (:require [fruits.regex.api  :as regex]
              [io.api            :as io]
              [source-code-statistics.config :as config]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-file-list
  ; @ignore
  ;
  ; @param (string) source-path
  ; @param (map) options
  ; {:filename-pattern (regex pattern)(opt)}
  ;
  ; @return (strings in vector)
  [source-path {:keys [filename-pattern] :or {filename-pattern config/DEFAULT-FILENAME-PATTERN}}]
  (io/search-files source-path filename-pattern))

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
  {:char-count    (count          source-code)
   :def-count     (regex/re-count source-code #"\(def\s")
   :defn-count    (regex/re-count source-code #"\(defn[\s\-]")
   :newline-count (regex/re-count source-code #"[\n\r]")
   :ns-count      (regex/re-count source-code #"\(ns\s")})

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
  {:char-count    (count          file-content)
   :newline-count (regex/re-count file-content #"\n\r")})
