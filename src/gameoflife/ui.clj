(ns gameoflife.ui
  (:require [gameoflife.grid :as grid]
            [gameoflife.core :as core]
            [clojure.tools.trace :as tools]))
(def fpg (atom nil))


(defn close! []
  (if-let [f (:frame @fpg)]
    (do (.setVisible f false)
        (.dispose f)
        (reset! fpg nil))))

(defn open! [title width height]
  (if @fpg (close!)
   (let [f (javax.swing.JFrame. title)
         _ (.setSize f width (+ 24 height))
         c (.getContentPane f)
         p (javax.swing.JPanel.)
         _ (.add c p)
         _ (.setVisible f true)
         g (.getGraphics p)
         i (.createImage p width height)
         h (.getGraphics i)]
     (reset! fpg {:frame f :panel p :graphics g
                  :oi i :og h}))))
(comment
  (open! "Game of life" 50 50)
  (close!))

(defn get-colored-state [s]
 (if (= s (:alive core/state))
    (. java.awt.Color WHITE) 
    (. java.awt.Color BLACK)))

(comment
  (get-colored-state "_")
  (get-colored-state "█"))
   

(defn display [grid]
  (let [display-size 500
        side (grid/size grid)]
    (open! (format "Game of life %sx%s grid" side side) display-size display-size)
    (doseq [tick (take 1 (core/ticks grid))]
      ;(doto (:og @fpg)
      ;  (.clearRect 0 0 side side);clears the board every tick
      (doseq [[coord state] (map-indexed
                             (fn [idx s] [(grid/idx->coord idx side) (get-colored-state s)])
                             tick)]
        (doto (:og @fpg)
          (.setColor state)
          (.fillRect (first coord) (second coord) 5 5)))
      (.drawImage (:graphics @fpg) (:oi @fpg) display-size display-size (:panel @fpg))
      (Thread/sleep 100))))
      
(comment
  (let [[[[x y] state]]
        (map-indexed
         (fn [idx s] [(grid/idx->coord idx core/grid-size) (get-colored-state s)])
         (take 1 (core/ticks core/initial-grid)))]
    [x y state])
  (map-indexed
   (fn [idx s] [(grid/idx->coord idx core/grid-size) (get-colored-state s)])
   (take 1 (core/ticks core/initial-grid)))
  (display core/initial-grid)

  (open! "Game of life" 1000 1000)
  (doto (:og @fpg)
    (.clearRect 0 0 800 800))
  (doto (:og @fpg)
    (.setColor (. java.awt.Color black))
    (.fillRect 200 200 100 100))
  (.drawImage (:graphics @fpg) (:oi @fpg) 500 500 (:panel @fpg))
  (close!)
  (doseq [[coord state]
          (map-indexed
           (fn [idx s] [(grid/idx->coord idx (grid/size core/initial-grid)) (get-colored-state s)])
           (take 1 (core/ticks core/initial-grid)))]
    (doto (:og @fpg)
      (.setColor (. java.awt.Color BLACK))
      (.fillRect (+ 100 (first coord)) (+ 100 (second coord)) 50 50))
    (.drawImage (:graphics @fpg) (:oi @fpg) 500 500 (:panel @fpg)))

  (doseq [tick (take 2000 (core/ticks core/initial-grid))]
        ;(doto (:og @fpg)
        ;  (.clearRect 0 0 side side);clears the board every tick
        (doseq [[coord state] (map-indexed
                               (fn [idx s] [(grid/idx->coord idx (grid/size core/initial-grid)) (get-colored-state s)])
                               tick)]
          (doto (:og @fpg)
            (.setColor state)
            (.fillRect (* 10(first coord)) (* 10 (second coord)) 10 10)))
        (.drawImage (:graphics @fpg) (:oi @fpg) 0 0 (:panel @fpg))
        (Thread/sleep 50)))
