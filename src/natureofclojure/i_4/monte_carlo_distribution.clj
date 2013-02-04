;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/introduction/MonteCarloDistribution/MonteCarloDistribution.pde
;;
(ns natureofclojure.i-4.monte-carlo-distribution
  (:require [quil.core :as qc]))

(def WIDTH 200)
(def HEIGHT 200)
(def vals (atom (vec (replicate WIDTH 0))))
(def norms (atom (vec (replicate WIDTH 0))))

(defn setup [])

(defn monte-carlo
  "An algorithm for picking a random number based on monte carlo method
   Here probability is determined by formula y = x"
  []
  (let [r1 (rand)
        r2 (rand)
        y (* r1 r1)]  ;; y = x * x (change for different results)
    ;; if r2 is valid, we'll use this one
    (if (< r2 y)
      r1
      (recur))))

(defn pick-random-values
  "Get random values"
  []
  (let [n (monte-carlo)
        index (int (* n WIDTH))]
    (swap! vals update-in [index] inc)))  

(defn normalize-values
  "Normalize vals if necessary"
  []
    (let [max-y (apply max @vals)
        is-normalized? (> max-y HEIGHT)]
      (dorun
       (for [x (range WIDTH)
             :let [v (nth @vals x)
                   y (if is-normalized?
                       (* HEIGHT (/ v max-y))
                       v)]]
         (swap! norms update-in [x] (constantly y))))))

(defn draw []
  (qc/background 100)
  (qc/stroke 255)
  (pick-random-values)
  (normalize-values)

  ;; Draw line:
  (dorun
   (for [x (range 0 WIDTH)]
     (qc/line x HEIGHT x (- HEIGHT (nth @norms x))))))

(defn run []
  (qc/defsketch monte-carlo-distribution
    :title "Monte Carlo Distribution"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))





