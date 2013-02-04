;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/introduction/MultipleProbability/MultipleProbability.pde
;;
(ns natureofclojure.i-2.multiple-probability
  (:require [quil.core :as qc]))

(def WIDTH 200.0)
(def HEIGHT 200.0)
(def x (atom 0))
(def y (atom 0))

(defn setup []
  (qc/background 0)
  (qc/smooth))

(defn draw []
  ;; create an alpha blended background
  (qc/fill 0 1)
  (qc/rect 0 0 WIDTH HEIGHT)

  ;; probabilities for 3 different cases (these need to add up to 100%
  ;; since something always occurs here!)
  ;;
  ;; 15% chance of black (we don't actually need this line since it is
  ;; by definition, the "in all other cases" part of our else)
  (let [p1 0.05         ;; 5% chance of pure white occurring
        p2 (+ 0.80 p1)  ;; 80% chance of gray occurring
        num (rand)]     ;; pick a random number between 0 and 1
    (cond (< num p1) (qc/fill 255)
          (< num p2) (qc/fill 150)
          :else (qc/fill 0)))
  (qc/stroke 200)
  (qc/rect @x @y 10 10)

  (swap! x #(mod (+ % 10) WIDTH))
  (when (= (int @x) 0)
    (swap! y #(mod (+ % 10) HEIGHT))))

(defn run []
  (qc/defsketch multiple-probability
    :title "Multiple Probability"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))