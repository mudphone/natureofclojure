;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp1_vectors/NOC_1_1_bouncingball_novectors/NOC_1_1_bouncingball_novectors.pde
;;
(ns natureofclojure.ch1-1-vectors.bouncingball-novectors
  (:require [quil.core :as qc]))

(def WIDTH 800.0)
(def HEIGHT 200.0)
(def x (atom 100))
(def y (atom 100))
(def x-speed (atom 2.5))
(def y-speed (atom 2))

(defn setup []
  (qc/smooth))

(defn draw []
  (qc/background 255)

  ;; Add the current speed to the location
  (swap! x + @x-speed)
  (swap! y + @y-speed)

  (when (or (> @x WIDTH)
            (< @x 0))
    (swap! x-speed * -1))
  (when (or (> @y HEIGHT)
            (< @y 0))
    (swap! y-speed * -1))

  ;; Display circle at x, y location
  (qc/stroke 0)
  (qc/stroke-weight 2)
  (qc/fill 127)
  (qc/ellipse @x @y 48 48))

(defn run []
  (qc/defsketch bouncingball-novectors
    :title "Bouncing Ball - No Vectors"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))