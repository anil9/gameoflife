(ns gameoflife.grid)

(defn neighbors [[x y]]
  {:sw [(dec x) (dec y)]
   :s [x (dec y)]
   :se [(inc x) (dec y)]
   :w [(dec x) y]
   :e [(inc x) y]
   :nw [(dec x) (inc y)]
   :n [x (inc y)]
   :ne [(inc x) (inc y)]})
(comment
  (neighbors [1 1]))

(defn sqrt [x]
  (int (Math/sqrt x)))

(defn visualize [grid]
  (partition (sqrt (count grid)) grid))

(defn generate [size]
  (vec (->> (range (* size size))
            (map (fn [_] (rand-int 2))))))

(defn coord->idx [[x y] grid-size]
  (+ x (* y grid-size)))

(comment
  (coord->idx [0 1] 5)
  (coord->idx [0 1] 100))

(defn idx->coord [idx grid-size]
  (let [x (mod idx grid-size)
        y (quot idx grid-size)]
    [x y]))
  
