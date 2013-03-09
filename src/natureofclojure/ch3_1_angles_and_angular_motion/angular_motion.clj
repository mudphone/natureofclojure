;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp3_oscillation/NOC_3_01_angular_motion/NOC_3_01_angular_motion.pde
;;
(ns natureofclojure.ch3-1-angles-and-angular-motion.angular-motion
  (:require [quil.core :as qc]))

(def WIDTH 800.0)
(def HEIGHT 200.0)
(def a (atom 0.0))
(def a-velocity (atom 0.0))

(defn setup [])

(defn angular-acceleration []
  ;;(qc/map-range (qc/mouse-x) 0.0  WIDTH -0.001, 0.001)
  0.0001)

(defn draw []
  (qc/background 255)
  (qc/stroke 0)
  (qc/stroke-weight 2)
  (qc/fill 127)

  (swap! a (partial + @a-velocity))
  (swap! a-velocity (partial + (angular-acceleration)))
  
  (qc/rect-mode :center)
  (qc/translate (/ WIDTH 2.0) (/ HEIGHT 2.0))
  (qc/rotate @a)
  (qc/line -60 0 60 0)
  (qc/ellipse 60 0 16 16)
  (qc/ellipse -60 0 16 16))

(defn run []
  (qc/defsketch angular-motion
    :title "Angular Motion"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))
