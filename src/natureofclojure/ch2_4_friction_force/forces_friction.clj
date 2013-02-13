;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp2_forces/NOC_2_4_forces_friction/NOC_2_4_forces_friction.pde
;;
(ns natureofclojure.ch2-4-friction-force.forces-friction
  (:require [quil.core :as qc])
  (:use [natureofclojure.ch2-4-friction-force.mover :as mover]
        [natureofclojure.math.vector :as mv]))

(def WIDTH 383.0)
(def HEIGHT 200.0)
(def movers (atom []))

(defn setup []
  (let [ms (vec
            (doall
             (for [i (range 20)
                   :let [mass (+ 1.0 (rand 3.0))]]
               (mover [0.0 0.0] mass))))]
    (swap! movers (constantly ms))))

(defn friction [m]
  (let [c 0.05
        v (:velocity m)]
    (if (= (mv/magnitude v) 0.0)
      [0.0 0.0]
      (->> (mv/multiply -1.0 v)
           (mv/normalize)
           (mv/multiply c)))))

(defn step-mover [m]
  (-> (mover/apply-force m (friction m))     ;; friction
      (mover/apply-force [0.01 0.0])         ;; wind
      (mover/apply-massless-force [0.0 0.1]) ;; gravity
      (mover/update)
      (mover/check-edges WIDTH HEIGHT)))

(defn draw []
  (qc/background 255)
  (let [stepped-movers (map step-mover @movers)]
    (dorun
     (map mover/display stepped-movers))
    (swap! movers (constantly stepped-movers))))

(defn run []
  (qc/defsketch forces-friction
    :title "Forces Friction"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))