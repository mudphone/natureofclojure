;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.ch3-1-angles-and-angular-motion.angles
  (:require [quil.core :as qc]))

(def WIDTH 640.0)
(def HEIGHT 360.0)
(def a (atom 0.0))
(def a-velocity (atom 0.0))

(defn setup [])

(defn angular-acceleration []
  (qc/map-range (qc/mouse-x) 0.0  WIDTH -0.001, 0.001))

(defn draw []
  (qc/background 255)

  (swap! a (partial + @a-velocity))
  (swap! a-velocity (partial + (angular-acceleration)))
  
  (qc/rect-mode :center)
  (qc/stroke 0)
  (qc/fill 127)
  (qc/translate (/ WIDTH 2.0) (/ HEIGHT 2.0))
  (qc/rotate @a)
  (qc/rect 0 0 64 36))

(defn run []
  (qc/defsketch angles
    :title "Angles"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))
