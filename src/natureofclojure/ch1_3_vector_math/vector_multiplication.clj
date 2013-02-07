;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp1_vectors/NOC_1_4_vector_multiplication/NOC_1_4_vector_multiplication.pde
;;
(ns natureofclojure.ch1-3-vector-math.vector-multiplication
  (:require [quil.core :as qc]))

(def WIDTH 800.0)
(def HEIGHT 200.0)

(defn setup [])

(defn draw []
  (qc/background 255)
  (qc/stroke-weight 2)
  (qc/stroke 0)
  (qc/no-fill)

  (qc/translate (/ WIDTH 2.0) (/ HEIGHT 2.0))
  (qc/ellipse 0 0 4 4)
  (let [mouse [(qc/mouse-x) (qc/mouse-y)]
        center [(/ WIDTH 2.0) (/ HEIGHT 2.0)]
        s (map - mouse center)
        m (map * (repeat 0.5) s)]
    (qc/line 0 0 (first m) (second m))))

(defn run []
  (qc/defsketch vector-multiplication
    :title "Vector Multiplication"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))