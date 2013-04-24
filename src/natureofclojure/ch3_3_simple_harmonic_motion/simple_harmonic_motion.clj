;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp3_oscillation/NOC_3_05_simple_harmonic_motion/NOC_3_05_simple_harmonic_motion.pde
;;
(ns natureofclojure.ch3-3-simple-harmonic-motion.simple-harmonic-motion
  (:require [quil.core :as qc]))

(def WIDTH  800.0)
(def HEIGHT 200.0)

(defn setup [])

(defn draw []
  (qc/background 255)

  (qc/stroke 0)
  (qc/stroke-weight 2)
  (qc/fill 127)
  
  (qc/translate (/ WIDTH 2.0) (/ HEIGHT 2.0))

  (let [amplitude 300
        period    120
        x (* amplitude (Math/cos (/ (* qc/TWO-PI (qc/frame-count)) period)))]
    (qc/line 0 0 x 0)
    (qc/ellipse x 0 48 48)))

(defn run []
  (qc/defsketch simple-harmonic-motion
    :title "Simple Harmonic Motion"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))
