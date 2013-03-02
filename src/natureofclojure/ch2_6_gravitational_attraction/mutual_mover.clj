;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp2_forces/NOC_2_8_mutual_attraction/Mover.pde
;;
(ns natureofclojure.ch2-6-gravitational-attraction.mutual-mover
  (:require [quil.core :as qc]
            [natureofclojure.math.vector :as mv]))

(def colors [[200,215,80],
             [190,90,212],
             [204,72,47],
             [130,187,205],
             [89,48,92],
             [64,92,59],
             [115,214,164],
             [218,154,57],
             [110,119,201],
             [93,47,35],
             [202,154,187],
             [199,70,103],
             [107,220,76],
             [197,129,106],
             [197,197,158],
             [91,156,67],
             [139,128,54],
             [75,87,102],
             [196,75,152]])

(defn init-mover
  ([location mass m]
     (init-mover [0.0 0.0] location mass m))
  ([velocity location mass m]
     (-> (assoc-in m [:location] location)
         (assoc-in [:velocity] velocity)
         (assoc-in [:acceleration] [0.0 0.0])
         (assoc-in [:mass] mass)
         (assoc-in [:color-rgb] (nth colors (rand (count colors)))))))

(defn mover
  ([]
     {:location []
      :velocity []
      :acceleration []
      :mass 1.0})
  ([location]
     (mover location 1.0))
  ([location mass]
     (init-mover location mass (mover)))
  ([velocity location mass]
     (init-mover velocity location mass (mover))))

(defn attract [m other-mover g]
  (let [f (mv/subtract (:location m) (:location other-mover))
        d (mv/magnitude f)
        d (qc/constrain d 5.0 25.0)
        n (mv/normalize f)
        strength (/ (* g (:mass m) (:mass other-mover)) (* d d))]
    (mv/multiply strength n)))

(defn apply-massless-force [m f]
  (update-in m [:acceleration] #(mv/add % f)))

(defn apply-force [m f]
  (let [f (mv/divide f (:mass m))]
    (apply-massless-force m f)))

(defn update [m]
  (-> (update-in m [:velocity] #(mv/add % (:acceleration m)))
      (update-in [:velocity] #(mv/limit 6.0 %))
      (update-in [:location] #(mv/add % (:velocity m)))
      (assoc-in [:acceleration] [0.0 0.0])))

(defn display [m]
  (qc/no-stroke)
  (apply #(qc/fill %1 %2 %3 200) (:color-rgb m))
  (let [d (* 16.0 (:mass m))]
    (apply #(qc/ellipse %1 %2 d d) (:location m))))
