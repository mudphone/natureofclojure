;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.i-5.perlin-noise
  (:require [quil.core :as qc]))

(def WIDTH 600.0)
(def HEIGHT 400.0)
(def t (atom 0))

(defn setup []
  (qc/frame-rate 60))

(defn draw []
  (qc/background 0)
  (qc/fill 255)
  (swap! t #(+ % 0.01))

  (let [n (qc/noise @t)
        x (qc/map-range n 0 1 0 WIDTH)]
    (qc/ellipse x (/ HEIGHT 2.0) 40 40)))

(defn run []
  (qc/defsketch perlin-noise
    :title "Perlin Noise"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))