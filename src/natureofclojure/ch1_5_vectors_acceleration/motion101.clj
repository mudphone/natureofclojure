;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp1_vectors/NOC_1_7_motion101/NOC_1_7_motion101.pde
;;
(ns natureofclojure.ch1-5-vectors-acceleration.motion101
  (:require [quil.core :as qc])
  (:use [natureofclojure.ch1-5-vectors-acceleration.mover.no-acceleration :as mover]))

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
    :title "Vectors: Acceleration - Motion 101"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))