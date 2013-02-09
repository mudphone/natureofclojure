;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp1_vectors/NOC_1_8_motion101_acceleration/NOC_1_8_motion101_acceleration.pde
;;
(ns natureofclojure.ch1-5-vectors-acceleration.motion101-acceleration
  (:require [quil.core :as qc])
  (:use [natureofclojure.ch1-5-vectors-acceleration.mover.acceleration :as mover]))

(def WIDTH 800.0)
(def HEIGHT 200.0)

(defn setup []
  (mover/initialize mover/mover WIDTH HEIGHT))

(defn draw []
  (qc/background 255)

  (mover/update mover/mover)
  (mover/check-edges mover/mover WIDTH HEIGHT)
  (mover/display @mover/mover))

(defn run []
  (qc/defsketch vectors-acceleration
    :title "Vectors: Acceleration"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))