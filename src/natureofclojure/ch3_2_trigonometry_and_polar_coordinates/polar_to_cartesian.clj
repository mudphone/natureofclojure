;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp3_oscillation/NOC_3_04_PolarToCartesian/NOC_3_04_PolarToCartesian.pde
;;
(ns natureofclojure.ch3-2-trigonometry-and-polar-coordinates.polar-to-cartesian
  (:require [quil.core :as qc]))

(def WIDTH 800.0)
(def HEIGHT 400.0)
(def theta (atom 0.0))
(def t (atom 0.0))

(defn setup [])

(defn draw []
  (qc/background 255)
  (qc/ellipse-mode :center)
  (qc/stroke 0)
  (qc/stroke-weight 2)
  
  (qc/translate (/ WIDTH 2.0) (/ HEIGHT 2.0))

  (let [n (qc/noise @t)
        r (* n 0.5 HEIGHT)
        x (* r (Math/cos @theta))
        y (* r (Math/sin @theta))
        d (* n 48)]
    (qc/fill (qc/map-range n 0.0 1.0 0.0 255.0))
    (qc/line 0 0 x y)
    (qc/ellipse x y d d)
    (swap! theta (partial + (qc/map-range (- 1.0 n) 0.0 1.0 0.0 0.08))))
  (swap! t (partial + 0.003)))

(defn run []
  (qc/defsketch polar-to-cartesian
    :title "Polar to Cartesian"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))
