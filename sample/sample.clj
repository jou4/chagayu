(use 'chagayu)
(import 'java.io.File)

(def sample-template
     "<p><%= (reduce * data) %><p>")
(def sample-file "sample/sample.txt")
(def sample-data {:data [1 2 3 4 5]})

(defn sample-1 []
  (println (parse sample-template sample-data)))

(defn sample-2 []
  (println (parse (File. sample-file) sample-data)))

(defn sample-3 []
  (println (parseFromFile sample-file sample-data)))


