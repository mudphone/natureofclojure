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
(def HEIGHT 200.0)
(def theta (atom 0.0))

(defn setup [])

(defn draw []
  (qc/background 255)
  (qc/ellipse-mode :center)
  (qc/stroke 0)
  (qc/stroke-weight 2)
  (qc/fill 127)
  
  (qc/translate (/ WIDTH 2.0) (/ HEIGHT 2.0))

  (let [r (* 0.45 HEIGHT)
        x (* r (Math/cos @theta))
        y (* r (Math/sin @theta))]
    (qc/line 0 0 x y)
    (qc/ellipse x y 48 48))
  
  (swap! theta (partial + 0.02)))

(defn run []
  (qc/defsketch polar-to-cartesian
    :title "Polar to Cartesian"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))
