(ns chagayu.core
  (:use [clojure.contrib.duck-streams :only [read-lines with-out-writer]])
  (:use [clojure.contrib.string :only [join split replace-str]])
  (:import java.io.CharArrayWriter))


(defn split-forms [s]
  (let [pre-read (fn [s] (replace-str "%>\n" "%>" s))]
    (map #(split #"<%" %) (split #"%>" (pre-read s)))))

(defn print-expr [s]
  (str "(print \"" s "\")"))

(defn program-expr [s]
  (when-not (nil? s)
    (if (= \= (first s))
      (str "(print " (subs s 1) ")")
      s)))

(defn gen-exprs [template]
  (let [fs (split-forms template)
        gen (fn [t] [(print-expr (first t)) (program-expr (second t))])]
    (join "" (filter #(not (nil? %)) (mapcat gen fs)))))

(defn parse [template data]
     (let [binds (map #(str (name (first %)) " " (second %)) data)
           pg (str "(let [" (join " " binds) "] " (gen-exprs template) ")")
           w (CharArrayWriter.)]
       (with-out-writer w (load-string pg))
       (.toString w)))
