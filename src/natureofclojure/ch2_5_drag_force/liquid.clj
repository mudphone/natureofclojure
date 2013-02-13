;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp2_forces/NOC_2_5_fluidresistance/Liquid.pde
;;
(ns natureofclojure.ch2-5-drag-force.liquid
  (:require [quil.core :as qc]
            [clojure.math.numeric-tower :as math]
            [natureofclojure.math.vector :as mv]))

(defn init [origin size c liq]
  (-> (assoc-in liq [:origin] origin)
      (assoc-in [:size] size)
      (assoc-in [:c] c)))

(defn liquid
  ([]
     {:origin []
      :size []
      :c 0.03})
  ([origin size c]
     (init origin size c (liquid))))

(defn origin-x [liq]
  (first (:origin liq)))

(defn origin-y [liq]
  (second (:origin liq)))

(defn size-w [liq]
  (first (:size liq)))

(defn size-h [liq]
  (second (:size liq)))

(defn contains? [liq location]
  (let [m-location location
        m-x (first location)
        m-y (second location)
        x (origin-x liq)
        y (origin-y liq)
        w (size-w liq)
        h (size-h liq)]
    (and (> m-x x)
         (< m-x (+ x w))
         (> m-y y)
         (< m-y (+ y h)))))

(defn drag [liq v]
  (let [speed (mv/magnitude v)]
    (if (= speed 0.0)
      [0.0 0.0]
      (let [c (:c liq)
            drag-magnitude (* c (math/expt speed 2))
            drag-force (mv/multiply -1.0 v)
            n (mv/normalize drag-force)]
        (mv/multiply drag-magnitude n)))))

(defn display [liq]
  (qc/no-stroke)
  (qc/fill 50)
  (qc/rect (origin-x liq) (origin-y liq)
           (size-w liq) (size-h liq)))