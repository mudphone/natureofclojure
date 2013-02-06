;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.i-5.exercise-i-10-noise-landscape
  (:require [quil.core :as qc]
            [natureofclojure.i-5.landscape :as ls]))

(def theta (atom 0.0))

(defn setup [])

(defn draw []
  (qc/background 255)
  (qc/push-matrix)
  (qc/translate (/ ls/WIDTH 2.0)
                (/ ls/HEIGHT 2.0)
                -160)
  (qc/rotate-x (/ Math/PI 3.0))
  (qc/rotate-z @theta)
  (ls/render @ls/landscape)
  (qc/pop-matrix)
  
  (ls/calculate! ls/landscape)
  (swap! theta #(+ % 0.0025)))

(defn run []
  (qc/defsketch noise-landscape
    :title "Noise Landscape"
    :setup setup
    :draw draw
    :size [ls/WIDTH ls/HEIGHT]
    :renderer :p3d))