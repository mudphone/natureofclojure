;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/introduction/NOC_I_4_Gaussian/NOC_I_4_Gaussian.pde
;;
(ns natureofclojure.i-3.gaussian
  (:import [java.util Random])
  (:require [quil.core :as qc]))

(def WIDTH 800)
(def HEIGHT 200)

;; Create a Java random number generating object
;; (This is a mutable thing.)
(def generator (new Random))

(defn setup []
  (qc/background 255)
  (qc/smooth))

(defn draw []
  (qc/no-stroke)
  (qc/fill 0 10)
  (qc/no-stroke)
  (let [sd 60.0
        mean (/ WIDTH 2.0)
        xloc (+ mean (* sd (. generator nextGaussian)))]
    (qc/ellipse xloc (/ HEIGHT 2.0) 16 16)))

(defn run []
  (qc/defsketch gaussian
    :title "Gaussian"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))