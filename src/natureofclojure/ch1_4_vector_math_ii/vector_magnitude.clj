;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/tree/master/Processing/chp1_vectors/NOC_1_5_vector_magnitude
;;
(ns natureofclojure.ch1-4-vector-math-ii.vector-magnitude
  (:require [quil.core :as qc]
            [clojure.math.numeric-tower :as math])
  (:use [natureofclojure.math.vector :as mv]))

(def WIDTH 800.0)
(def HEIGHT 200.0)

(defn setup [])

(defn draw []
  (qc/background 255)
  
  (let [mouse [(qc/mouse-x) (qc/mouse-y)]
        center [(/ WIDTH 2.0) (/ HEIGHT 2.0)]
        s (mv/subtract mouse center)
        m (mv/magnitude s)]
    (qc/fill 0)
    (qc/no-stroke)
    (qc/rect 0 0 m 10)

    (qc/translate (/ WIDTH 2.0) (/ HEIGHT 2.0))
    (qc/stroke 0)
    (qc/stroke-weight 2)
    (apply (partial qc/line 0 0) s)))

(defn run []
  (qc/defsketch vector-magnitude
    :title "Vector Magnitude"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))