;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp2_forces/NOC_2_5_fluidresistance/NOC_2_5_fluidresistance.pde
;;
(ns natureofclojure.ch2-5-drag-force.fluid-resistance
  (:require [quil.core :as qc]
            [natureofclojure.ch2-5-drag-force.mover :as mover]
            [natureofclojure.ch2-5-drag-force.liquid :as liquid]))

(def WIDTH 800.0)
(def HEIGHT 200.0)
(def movers (atom []))
(def liq (atom (liquid/liquid [0 (/ HEIGHT 2.0)]
                              [WIDTH (/ HEIGHT 2.0)]
                              0.1)))

(defn reset-movers! []
  (let [ms (vec
            (doall
             (for [i (range 20)
                   :let [mass (+ 1.0 (rand 3.0))]]
               (mover/mover [(rand WIDTH) 0.0] mass))))]
    (swap! movers (constantly ms))))

(defn setup []
  (reset-movers!))

(defn step-mover [m]
  (let [drag-force (if (liquid/contains? @liq (:location m))
                     (liquid/drag @liq (:velocity m))
                     [0.0 0.0])]
    (-> (mover/apply-force m drag-force)    ;; friction
        (mover/apply-force [0.01 0.0])        ;; wind
        (mover/apply-massless-force [0.0 0.1]) ;; gravity
        (mover/update)
        (mover/check-edges WIDTH HEIGHT))))

(defn draw []
  (qc/background 255)
  (liquid/display @liq)
  (let [stepped-movers (map step-mover @movers)]
    (dorun
     (map mover/display stepped-movers))
    (swap! movers (constantly stepped-movers))))

(defn mouse-pressed []
  (reset-movers!))

(defn run []
  (qc/defsketch fluid-resistance
    :title "Fluid Resistance"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]
    :mouse-pressed mouse-pressed))