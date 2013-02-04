;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/introduction/SimpleProbablility/SimpleProbablility.pde
;;
(ns natureofclojure.i-2.simple-probability
  (:require [quil.core :as qc]))

(def WIDTH 200.0)
(def HEIGHT 200.0)
(def x (atom 0))
(def y (atom 0))

(defn setup []
  (qc/background 0)
  (qc/smooth))

(defn draw []
  (qc/fill 0 1)
  (qc/rect 0 0 WIDTH HEIGHT)

  (let [prob (/ (qc/mouse-x) WIDTH)
        r (rand)]
    (when (< r prob)
      (qc/no-stroke)
      (qc/fill 255)
      (qc/ellipse @x @y 10 10)))

  (swap! x #(mod (+ % 10) WIDTH))
  (when (= (int @x) 0)
    (swap! y #(mod (+ % 10) HEIGHT))))

(defn run []
  (qc/defsketch simple-probability
    :title "Simple Probability"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))