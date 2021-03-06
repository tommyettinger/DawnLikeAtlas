;; meant to be pasted into a REPL
(def text (slurp "Dawnlike.atlas")) 
(doseq [n [2 3 4]] (spit (str "Dawnlike" n ".atlas") (-> text (clojure.string/replace "Dawnlike.png" (str "Dawnlike" n ".png")) (clojure.string/replace #"(\d+),(?: ?)(\d+)" (fn [[_ l r]] (str (* (Integer/parseInt l) n) "," (* (Integer/parseInt r) n)))))))
(def fnt (slurp "font.fnt"))
(doseq [n [2 3 4]] (spit (str "font" n ".fnt") (clojure.string/replace fnt #" x=([\-0-9]+) y=([\-0-9]+) width=([\-0-9]+) height=([\-0-9]+) xoffset=([\-0-9]+) yoffset=([\-0-9]+) xadvance=([\-0-9]+)" (fn[[_ x y w h xo yo xa]] (str " x="(* n (read-string x)) " y="(* n (read-string y)) " width="(* n (read-string w)) " height="(* n (read-string h)) " xoffset="(* n (read-string xo)) " yoffset="(* n (read-string yo)) " xadvance="(* n (read-string xa)) )))))
