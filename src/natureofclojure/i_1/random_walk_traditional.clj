;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/tree/master/Processing/introduction/NOC_I_1_RandomWalkTraditional
;;
(ns natureofclojure.i-1.random-walk-traditional
  (:require [quil.core :as qc]
            [natureofclojure.i-1.walker.traditional :as wt]))

(defn setup []
  (qc/frame-rate 60)
  (qc/background 255)
  (qc/smooth))

(defn draw []
  (wt/step-walker wt/walker)
  (wt/render-walker @wt/walker))

(defn run []
  (qc/defsketch random-walk-traditional
    :title "Random Walk Traditional"
    :setup setup
    :draw draw
    :size [wt/WIDTH wt/HEIGHT]))