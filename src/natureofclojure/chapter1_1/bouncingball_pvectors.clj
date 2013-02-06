;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp1_vectors/NOC_1_2_bouncingball_vectors/NOC_1_2_bouncingball_vectors.pde
;;
(ns natureofclojure.chapter1-1.bouncingball-pvectors
  (:import [processing.core PVector])
  (:require [quil.core :as qc]))

(def WIDTH 200.0)
(def HEIGHT 200.0)
(def location (new PVector 100 100))
(def velocity (new PVector 2.5 2))

(defn setup []
  (qc/background 255))

(defn draw []
  (qc/no-stroke)
  (qc/fill 255, 10)
  (qc/rect 0 0 WIDTH HEIGHT)

  ;; Add the current speed to the location
  (.add location velocity)

  (when (or (> (.x location) WIDTH)
            (< (.x location) 0))
    (set! (. velocity x) (* -1 (.x velocity))))
  (when (or (> (.y location) HEIGHT)
            (< (.y location) 0))
    (set! (. velocity y) (* -1 (.y velocity))))

  ;; Display circle at x, y location
  (qc/stroke 0)
  (qc/fill 175)
  (qc/ellipse (.x location) (.y location) 16 16))

(defn run []
  (qc/defsketch bouncingball-pvectors
    :title "Bouncing Ball - PVectors"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))