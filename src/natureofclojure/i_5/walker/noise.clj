;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/introduction/NOC_I_5_NoiseWalk/Walker.pde
;;
(ns natureofclojure.i-5.walker.noise
  (:require [quil.core :as qc]))

(def WIDTH 800.0)
(def HEIGHT 200.0)
(def walker (atom {:location [(/ WIDTH 2.0)
                              (/ HEIGHT 2.0)]
                   :n-off [(* 1000 (rand))
                           (* 1000 (rand))]}))

(defn update-walker [w]
  (let [n-off-x (first (:n-off w))
        n-off-y (second (:n-off w))
        x (qc/map-range (qc/noise n-off-x) 0 1 0 WIDTH)
        y (qc/map-range (qc/noise n-off-y) 0 1 0 HEIGHT)]
    {:location [x y]
     :n-off (map + (repeat 0.01) (:n-off w))}))

(defn walk-walker [w-atom]
  (swap! w-atom update-walker)
)

(defn display-walker [w]
  (qc/stroke-weight 2)
  (qc/fill 127)
  (qc/stroke 0)
  (qc/ellipse (first (:location w))
              (second (:location w))
              48, 48))