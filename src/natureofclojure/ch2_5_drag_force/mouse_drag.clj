;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.ch2-5-drag-force.mouse-drag
  (:require [quil.core :as qc]
            [clojure.math.numeric-tower :as math])
  (:use [natureofclojure.ch2-5-drag-force.mover :as mover]
        [natureofclojure.math.vector :as mv]))

(def WIDTH 800.0)
(def HEIGHT 200.0)
(def movers (atom []))
(def mouse-pressed? (atom false))

(defn setup []
  (let [ms (vec
            (doall
             (for [i (range 20)
                   :let [mass (+ 1.0 (rand 3.0))]]
               (mover [0.0 0.0] mass))))]
    (swap! movers (constantly ms))))

(defn drag [m]
  (let [c -0.03
        v (:velocity m)
        speed (mv/magnitude v)]
    (if (= speed 0.0)
      [0.0 0.0]
      (->> (mv/normalize v)
           (mv/multiply (* c (math/expt speed 2)))))))

(defn step-mover [m]
  (let [m (if @mouse-pressed?
            (mover/apply-force m (drag m))    ;; drag
            m)]
   (-> (mover/apply-force m [0.01 0.0])       ;; wind
       (mover/apply-massless-force [0.0 0.1]) ;; gravity
       (mover/update)
       (mover/check-edges WIDTH HEIGHT))))

(defn draw []
  (qc/background 255)
  (let [stepped-movers (map step-mover @movers)]
    (dorun
     (map mover/display stepped-movers))
    (swap! movers (constantly stepped-movers))))

(defn mouse-pressed []
  (swap! mouse-pressed? (constantly true)))

(defn mouse-released []
  (swap! mouse-pressed? (constantly false)))

(defn run []
  (qc/defsketch forces-friction
    :title "Forces Friction"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]
    :mouse-pressed mouse-pressed
    :mouse-released mouse-released))