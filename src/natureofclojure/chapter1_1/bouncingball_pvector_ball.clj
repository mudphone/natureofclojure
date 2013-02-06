;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.chapter1-1.bouncingball-pvector-ball
  (:import [processing.core PVector])
  (:require [quil.core :as qc]
            [natureofclojure.chapter1-1.ball.pvector-ball :as pb]))

(defn setup [])

(defn draw []
  (qc/background 255)
  (pb/move @pb/ball)
  (pb/bounce @pb/ball)
  (pb/display @pb/ball))

(defn run []
  (qc/defsketch bouncingball-pvector-ball
    :title "Bouncing Ball - PVector Ball"
    :setup setup
    :draw draw
    :size [pb/WIDTH pb/HEIGHT]))