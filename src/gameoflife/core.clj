(ns gameoflife.core
  (:gen-class)
  (:require [gameoflife.grid :as grid]
            [clojure.pprint :as p]))

(def state {:dead "_"
            :alive "█"})
(def grid-size 100)
(comment
  (prn state))

(def initial-grid (mapv #(if (= 0 %)
                           (:dead state)
                           (:alive state))
                        (grid/generate grid-size)))

(comment
  (prn initial-grid)
  (grid/visualize initial-grid))

(comment
  (grid/neighbors [1 1]))

(defn is-alive? [grid idx]
  (= (:alive state) (get grid idx)))

(comment
  (is-alive? initial-grid 0)
  (get initial-grid 0)
  (type initial-grid))

(defn living-neighbors [neighbors grid]
  (->> (vals neighbors)
       (filter #(is-alive? grid (grid/coord->idx % grid-size)))))

(comment
  (living-neighbors (grid/neighbors [1 1]) initial-grid)
  (count (living-neighbors (grid/neighbors [1 1]) initial-grid))
  (->> (vals (grid/neighbors [1 1]))
       (map #(get initial-grid (grid/coord->idx % grid-size)))))

(defn tick-cell [idx grid]
  (let [coord (grid/idx->coord idx (count grid))
        n (grid/neighbors coord)
        alive? (is-alive? grid idx)
        alive-neighbors (count (living-neighbors n grid))]
    (cond
      alive? (if (or (= alive-neighbors 2) (= alive-neighbors 3))
               (:alive state)
               (:dead state))
      (not alive?) (if (= alive-neighbors 3)
                     (:alive state)
                     (:dead state)))))

(comment
  (tick-cell 7 initial-grid))

(defn tick [grid]
  (->> (range (count grid))
       (mapv #(tick-cell % grid))))

(defn ticks [initial-grid]
  (concat (iterate tick initial-grid)))

(comment
  (grid/visualize (first (ticks initial-grid)))
  (grid/visualize (second (ticks initial-grid)))
  (type (take 10 (ticks initial-grid))))


(defn game-loop [starting-grid loops]
  (let [delay (quot 1000 10)]
    (loop [grid starting-grid
           n loops]
      (print
       (with-out-str
         (p/pprint (grid/visualize grid))
         (println "\n")))
      (flush)
      (Thread/sleep delay)
      (if (= 0 n) nil
          (recur (tick grid) (dec n))))))

(comment
  ; one tick
  (p/pprint (grid/visualize (tick initial-grid)))
  ; running game loop n iterations
  (game-loop initial-grid 100))

(defn -main
  [& args]
  "doing nothing")
