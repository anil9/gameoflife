(ns gameoflife.ui
  (:require [gameoflife.grid :as grid]
            [gameoflife.core :as core]))
(def fpg (atom nil))


(defn close! []
  (when-let [f (:frame @fpg)]
     (.setVisible f false)
     (.dispose f)
     (reset! fpg nil)))

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
  (let [display-size 1000
        side (grid/size grid)]
    (open! (format "Game of life %sx%s grid" side side) display-size display-size)
    (doseq [tick (take 2000 (core/ticks grid))]
      (doseq [[coord state] (map-indexed
                             (fn [idx s] [(grid/idx->coord idx (grid/size core/initial-grid)) (get-colored-state s)])
                             tick)]
        (doto (:og @fpg)
          (.setColor state)
          (.fillRect (* 10 (first coord)) (* 10 (second coord)) 10 10))) ; each cell is a 10x10 rectangle
      (.drawImage (:graphics @fpg) (:oi @fpg) 0 0 (:panel @fpg))
      (Thread/sleep 10))))
      
(comment
  (display core/initial-grid))

(defn -main
  [& args]
  (display core/initial-grid))
