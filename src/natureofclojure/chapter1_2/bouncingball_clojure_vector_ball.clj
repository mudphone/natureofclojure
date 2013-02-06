;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.chapter1-2.bouncingball-clojure-vector-ball
  (:require [quil.core :as qc]
            [natureofclojure.chapter1-2.ball.clojure-vector-ball :as cb]))

(defn setup [])

(defn draw []
  (qc/background 255)
  (cb/move cb/ball)
  (cb/bounce cb/ball)
  (cb/display @cb/ball))

(defn run []
  (qc/defsketch bouncingball-clojure-vector-ball
    :title "Bouncing Ball - Clojure Vector Ball"
    :setup setup
    :draw draw
    :size [cb/WIDTH cb/HEIGHT]))