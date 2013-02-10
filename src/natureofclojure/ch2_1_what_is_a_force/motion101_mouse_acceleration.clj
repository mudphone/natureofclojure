;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.ch2-1-what-is-a-force.motion101-mouse-acceleration
  (:require [quil.core :as qc])
  (:use [natureofclojure.ch2-1-what-is-a-force.mover.mouse-acceleration :as mover]))

(def WIDTH 800.0)
(def HEIGHT 200.0)

(defn setup []
  (mover/initialize mover/mover WIDTH HEIGHT))

(defn draw []
  (qc/background 255)

  (mover/update mover/mover)
  (mover/display @mover/mover))

(defn run []
  (qc/defsketch what-is-a-force
    :title "What is a force?"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))