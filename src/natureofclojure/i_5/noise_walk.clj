;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/introduction/NOC_I_5_NoiseWalk/NOC_I_5_NoiseWalk.pde
;;
(ns natureofclojure.i-5.noise-walk
  (:require [quil.core :as qc]
            [natureofclojure.i-5.walker.noise :as wn]))

(defn setup []
  (qc/frame-rate 30))

(defn draw []
  (qc/background 255)
  (wn/walk-walker wn/walker)
  (wn/display-walker @wn/walker))

(defn run []
  (qc/defsketch noise-walk
    :title "Noise Walk"
    :setup setup
    :draw draw
    :size [wn/WIDTH wn/HEIGHT]))