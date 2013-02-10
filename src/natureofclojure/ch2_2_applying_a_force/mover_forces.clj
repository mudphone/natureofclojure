;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp2_forces/NOC_2_1_forces/Mover.pde
;;
(ns natureofclojure.ch2-2-applying-a-force.mover-forces
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

(defn add-force [f m]
  (let [f (mv/divide f (:mass m))]
    (update-in m [:acceleration] #(mv/add % f))))

(defn apply-force [f m-atom]
  (swap! m-atom #(add-force f %)))

(defn update-mover [m]
  (-> (update-in m [:velocity] #(mv/add % (:acceleration m)))
      (update-in [:velocity] #(mv/limit 5.0 %))
      (update-in [:location] #(mv/add % (:velocity m)))))

(defn update [m-atom]
  (swap! m-atom update-mover)
  (swap! m-atom #(assoc-in % [:acceleration] [0.0 0.0])))

(defn display [m]
  (qc/stroke 0)
  (qc/stroke-weight 2)
  (qc/fill 127)
  (apply #(qc/ellipse %1 %2 48 48) (:location m)))

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

(defn check-edges [m-atom width height]
  (cond (> (loc-x @m-atom) width) (swap! m-atom #(bounce-x-right % width))
        (< (loc-x @m-atom) 0) (swap! m-atom bounce-x-left))
  (while (> (loc-y @m-atom) height)
    (swap! m-atom #(bounce-y-bottom % height))))