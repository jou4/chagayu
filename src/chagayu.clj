(ns chagayu
  (:use [clojure.contrib.duck-streams :only [read-lines]])
  (:use [clojure.contrib.string :only [join]])
  (:require [chagayu.core :as core])
  (:import java.io.File))


(defn- file-contents [f]
  (join "\n" (read-lines f)))


(defmulti
  #^{:doc "Parse template text and return generate text."
     :arglists '([template] [template & data])}
  parse (fn [template & data] (class template)))

(defmethod parse File
  ([#^File f] (parse f nil))
  ([#^File f data] (parse (file-contents f) data)))

(defmethod parse String
  ([template] (parse template nil))
  ([template data] (core/parse template data)))

(defmethod parse :default [x & xs]
  (throw (Exception. (str "Cannot parse <" (pr-str x) "> as a template."))))

(defn parseFromFile
  ([f] (parseFromFile f nil))
  ([f data] (parse (File. f) data)))
