;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/introduction/RandomWalkTrail/RandomWalkTrail.pde
;;
(ns natureofclojure.i-2.random-walk-trail
  (:require [quil.core :as qc]
            [natureofclojure.i-2.walker.trail :as tr]))

(defn setup []
  (qc/frame-rate 30))

(defn draw []
  (qc/background 255)
  (tr/step-walker tr/walker)
  (tr/render-walker @tr/walker))

(defn run []
  (qc/defsketch random-walk-trail
    :title "Random Walk Trail"
    :setup setup
    :draw draw
    :size [tr/WIDTH tr/HEIGHT]))