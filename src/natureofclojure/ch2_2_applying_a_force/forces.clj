;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp2_forces/NOC_2_1_forces/NOC_2_1_forces.pde
;;
(ns natureofclojure.ch2-2-applying-a-force.forces
  (:require [quil.core :as qc])
  (:use [natureofclojure.ch2-2-applying-a-force.mover-forces :as mover]))

(def WIDTH 800.0)
(def HEIGHT 200.0)

(defn setup []
  (mover/initialize mover/mover WIDTH HEIGHT))

(defn draw []
  (qc/background 255)

  ;; wind
  (mover/apply-force [0.01, 0.0] mover/mover)

  ;; gravity
  (mover/apply-force [0.0, 0.1] mover/mover)
  
  (mover/update mover/mover)
  (mover/display @mover/mover)
  (mover/check-edges mover/mover WIDTH HEIGHT))

(defn run []
  (qc/defsketch what-is-a-force
    :title "What is a force?"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))