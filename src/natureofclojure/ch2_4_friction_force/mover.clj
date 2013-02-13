;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp2_forces/NOC_2_4_forces_friction/Mover.pde
;;
(ns natureofclojure.ch2-4-friction-force.mover
  (:require [quil.core :as qc])
  (:use [natureofclojure.math.vector :as mv]))

(defn init-mover [location mass m]
  (-> (assoc-in m [:location] location)
      (assoc-in [:velocity] [0.0 0.0])
      (assoc-in [:acceleration] [0.0 0.0])
      (assoc-in [:mass] mass)))

(defn mover
  ([]
     {:location []
      :velocity []
      :acceleration []
      :mass 1.0})
  ([location]
     (mover location 1.0))
  ([location mass]
     (init-mover location mass (mover))))

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
  (qc/stroke 0)
  (qc/stroke-weight 2)
  (qc/fill 127 100)
  (let [d (* 16.0 (:mass m))]
    (apply #(qc/ellipse %1 %2 d d) (:location m))))

(defn loc-x [m]
  (first (:location m)))

(defn loc-y [m]
  (second (:location m)))

(defn vel-x [m]
  (first (:velocity m)))

(defn vel-y [m]
  (second (:velocity m)))

(defn bounce-x-right [m width]
  (-> (assoc-in m [:location] [width
                               (loc-y m)])
      (assoc-in [:velocity] [(* -1.0 (vel-x m))
                             (vel-y m)])))

(defn bounce-x-left [m]
  (-> (assoc-in m [:location] [0.0 (loc-y m)])
      (assoc-in [:velocity] [(* -1.0 (vel-x m))
                             (vel-y m)])))

(defn bounce-y-bottom [m height]
  (-> (assoc-in m [:location] [(loc-x m)
                               height])
      (assoc-in [:velocity] [(vel-x m)
                             (* -1.0 (vel-y m))])))

(defn check-edges [m width height]
  (let [m (cond (> (loc-x m) width) (bounce-x-right m width)
                (< (loc-x m) 0) (bounce-x-left m)
                :else m)]
    (if (> (loc-y m) height)
      (bounce-y-bottom m height)
      m)))