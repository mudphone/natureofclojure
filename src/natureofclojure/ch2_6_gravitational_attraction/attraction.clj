;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp2_forces/NOC_2_6_attraction/NOC_2_6_attraction.pde
;;
(ns natureofclojure.ch2-6-gravitational-attraction.attraction
  (:require [quil.core :as qc]
            [natureofclojure.ch2-6-gravitational-attraction.mover :as mover]
            [natureofclojure.ch2-6-gravitational-attraction.attractor :as attractor]))

(def WIDTH 640.0)
(def HEIGHT 360.0)
(def movers (atom []))
(def attractor (atom nil))

(defn reset-movers! []
  (let [ms (vec
            (doall
             (for [i (range 1)
                   :let [mass (+ 1.0 (rand 3.0))]]
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
  (qc/background 255)
  (let [stepped-movers (map step-mover @movers)]
    (dorun
     (map mover/display stepped-movers))
    (attractor/display @attractor)
    (swap! movers (constantly stepped-movers))))

(defn mouse-pressed []
  (reset-movers!))

(defn run []
  (qc/defsketch gravitational-attraction
    :title "Gravitational Attraction"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]
    :mouse-pressed mouse-pressed))
