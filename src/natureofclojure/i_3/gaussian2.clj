;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/introduction/Gaussian2/Gaussian2.pde
;;
(ns natureofclojure.i-3.gaussian2
  (:import [java.util Random])
  (:require [quil.core :as qc]))

(def WIDTH 200)
(def HEIGHT 200)

;; Create a Java random number generating object
;; (This is a mutable thing.)
(def generator (new Random))

(defn setup []
  (qc/background 0)
  (qc/smooth))

(defn scale-by-std [v sd mean]
  (+ (* v sd) mean))

(defn gauss-color [sd mean]
  (let [c (float (. generator nextGaussian))]
    (qc/constrain (scale-by-std c sd mean) 0 255)))

(defn draw []
  (qc/fill 0 1)
  (qc/rect 0 0 WIDTH HEIGHT)
  (qc/no-stroke)
  (let [sd 100
        mean 100
        r (gauss-color sd mean)
        g (gauss-color sd mean)
        b (gauss-color sd mean)
        sd-xy (/ WIDTH 10.0)
        mean-xy (/ WIDTH 2.0)
        x-loc (scale-by-std (float (. generator nextGaussian))
                            sd-xy
                            mean-xy)
        y-loc (scale-by-std (float (. generator nextGaussian))
                            sd-xy
                            mean-xy)]
    (qc/fill r g b)
    (qc/ellipse x-loc y-loc 8 8)))

(defn run []
  (qc/defsketch gaussian2
    :title "Gaussian 2"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))