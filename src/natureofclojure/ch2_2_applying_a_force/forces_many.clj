;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp2_forces/NOC_2_2_forces_many/NOC_2_2_forces_many.pde
;;
(ns natureofclojure.ch2-2-applying-a-force.forces-many
  (:require [quil.core :as qc])
  (:use [natureofclojure.ch2-2-applying-a-force.mover-forces :as mover]))

(def WIDTH 800.0)
(def HEIGHT 200.0)
(def movers (atom []))

(defn setup []
  (let [ms (vec
            (doall
             (for [i (range 20)
                   :let [mass (+ 0.1 (rand 3.9))]]
               (mover [0.0 0.0] mass))))]
    (swap! movers (constantly ms))))

(defn step-mover [m]
  (-> (mover/apply-force m [0.01 0.0]) ;; wind
      (mover/apply-force [0.0 0.1])    ;; gravity
      (mover/update)
      (mover/check-edges WIDTH HEIGHT)))

(defn draw []
  (qc/background 255)
  (let [stepped-movers (map step-mover @movers)]
    (dorun
     (map mover/display stepped-movers))
    (swap! movers (constantly stepped-movers))))

(defn run []
  (qc/defsketch forces-many
    :title "Forces: Many"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))