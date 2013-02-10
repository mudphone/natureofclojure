;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp1_vectors/NOC_1_10_motion101_acceleration/NOC_1_10_motion101_acceleration.pde
;;
(ns natureofclojure.ch1-6-vectors-acceleration-towards-mouse.motion101-mouse-acceleration
  (:require [quil.core :as qc])
  (:use [natureofclojure.ch1-6-vectors-acceleration-towards-mouse.mover.mouse-acceleration :as mover]))

(def WIDTH 800.0)
(def HEIGHT 200.0)

(defn setup []
  (mover/initialize mover/mover WIDTH HEIGHT))

(defn draw []
  (qc/background 255)

  (mover/update mover/mover)
  (mover/display @mover/mover))

(defn run []
  (qc/defsketch vectors-mouse-acceleration
    :title "Vectors: Mouse Acceleration"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))