;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/tree/master/Processing/introduction/NOC_I_3_RandomWalkTendsToRight
;;
(ns natureofclojure.i-2.random-walk-tends-to-right
  (:require [quil.core :as qc]
            [natureofclojure.i-2.walker.tends-to-right :as ttr]))

(defn setup []
  (qc/frame-rate 60)
  (qc/background 255)
  (qc/smooth))

(defn draw []
  (ttr/pull-walker ttr/walker)
  (ttr/render-walker @ttr/walker))

(defn run []
  (qc/defsketch random-walk-tends-to-right
    :title "Random Walk Tends to Right"
    :setup setup
    :draw draw
    :size [ttr/WIDTH ttr/HEIGHT]))