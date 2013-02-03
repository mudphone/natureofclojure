(ns natureofclojure.i-3.gaussian-throb
  (:import [java.util Random])
  (:require [quil.core :as qc]))

(def WIDTH 400)
(def HEIGHT 300)

;; Create a Java random number generating object
;; (This is a mutable thing.)
(def generator (new Random))

(defn setup [])

(defn draw []
  (qc/background 255)
  (let [h (+ 100 (* 10 (. generator nextGaussian)))]
    (qc/fill 0)
    (qc/ellipse (/ WIDTH 2.0) (/ HEIGHT 2.0) h h)))

(defn run []
  (qc/defsketch gaussian-throb
    :title "Gaussian Throb"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))








