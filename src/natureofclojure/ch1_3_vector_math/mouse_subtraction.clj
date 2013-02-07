;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.ch1-3-vector-math.mouse-subtraction
  (:import [processing.core PVector])
  (:require [quil.core :as qc]))

(def WIDTH 500.0)
(def HEIGHT 300.0)

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
        s (map - mouse center)]
    (qc/line 0 0 (first s) (second s))))

(defn run []
  (qc/defsketch mouse-subtraction
    :title "Mouse Subtraction"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))