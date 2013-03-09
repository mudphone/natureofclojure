;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp3_oscillation/NOC_3_02_forces_angular_motion/NOC_3_02_forces_angular_motion.pde
;;
(ns natureofclojure.ch3-1-angles-and-angular-motion.forces-angular-motion
  (:require [quil.core :as qc]
            [natureofclojure.ch3-1-angles-and-angular-motion.mover :as mover]
            [natureofclojure.ch3-1-angles-and-angular-motion.attractor :as attractor]))

(def WIDTH 800.0)
(def HEIGHT 200.0)
(def movers (atom []))
(def attractor (atom nil))

(defn reset-movers! []
  (let [ms (vec
            (doall
             (for [i (range 10)
                   :let [mass (+ 0.1 (rand 1.9))]]
               (mover/mover [1.0 0.0] [(rand WIDTH) (rand HEIGHT)] mass))))]
    (swap! movers (constantly ms))))

(defn setup []
  (reset-movers!)
  (swap! attractor (constantly
                    (attractor/attractor [(/ WIDTH 2.0) (/ HEIGHT 2.0)]))))

(defn step-mover [m]
  (let [attractive-force (attractor/attract @attractor m)]
    (-> (mover/apply-force m attractive-force)
        (mover/update))))

(defn draw []
  (qc/background 0)
  (let [stepped-movers (map step-mover @movers)]
    (dorun
     (map mover/display stepped-movers))
    (attractor/display @attractor)
    (swap! movers (constantly stepped-movers))))

(defn mouse-pressed []
  (reset-movers!))

(defn run []
  (qc/defsketch forces-angular-motion
    :title "Forces: Angular Motion"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]
    :mouse-pressed mouse-pressed))
