;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/tree/master/Processing/introduction/NOC_I_1_RandomWalkTraditional
;;
(ns natureofclojure.i-1.random-walk-traditional
  (:require [quil.core :as qc]))

(def WIDTH 800)
(def HEIGHT 600)
(def walker (atom {:x (/ WIDTH 2.0)
                   :y (/ HEIGHT 2.0)}))

(defn step-walker [w-atom]
  (let [choice (rand-int 4)]
    (cond (= choice 0) (swap! w-atom update-in [:x] #(qc/constrain-float (+ % 1) 0 WIDTH))
          (= choice 1) (swap! w-atom update-in [:x] #(qc/constrain-float (- % 1) 0 WIDTH))
          (= choice 2) (swap! w-atom update-in [:y] #(qc/constrain-float (+ % 1) 0 HEIGHT))
          (= choice 3) (swap! w-atom update-in [:y] #(qc/constrain-float (- % 1) 0 HEIGHT)))))

(defn render-walker-at-position [position]
  (qc/stroke 0)
  (qc/point (:x position) (:y position)))

(defn setup []
  (qc/frame-rate 60)
  (qc/background 255)
  (qc/smooth))

(defn draw []
  (step-walker walker)
  (render-walker-at-position @walker))

(defn run []
  (qc/defsketch random-walk-traditional
    :title "Random Walk Traditional"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))