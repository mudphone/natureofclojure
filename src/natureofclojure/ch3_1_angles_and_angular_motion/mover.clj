;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp3_oscillation/NOC_3_02_forces_angular_motion/Mover.pde
;;
(ns natureofclojure.ch3-1-angles-and-angular-motion.mover
  (:require [quil.core :as qc]
            [natureofclojure.math.vector :as mv]))

(defn init-mover
  ([location mass m]
     (init-mover [0.0 0.0] location mass m))
  ([velocity location mass m]
     (-> (assoc-in m [:location] location)
         (assoc-in [:velocity] velocity)
         (assoc-in [:acceleration] [0.0 0.0])
         (assoc-in [:mass] mass))))

(defn mover
  ([]
     {:location []
      :velocity []
      :acceleration []
      :mass 1.0
      :angle 0.0
      :a-velocity 0.0
      :a-acceleration 0.0})
  ([location]
     (mover location 1.0))
  ([location mass]
     (init-mover location mass (mover)))
  ([velocity location mass]
     (init-mover velocity location mass (mover))))

(defn apply-massless-force [m f]
  (update-in m [:acceleration] #(mv/add % f)))

(defn apply-force [m f]
  (let [f (mv/divide f (:mass m))]
    (apply-massless-force m f)))

(defn update-velocity [m]
  (-> (update-in m [:velocity] #(mv/add % (:acceleration m)))
      (update-in [:velocity] #(mv/limit 6.0 %))))

(defn update-location [m]
  (update-in m [:location] #(mv/add % (:velocity m))))

(defn update-angular-acceleration [m]
  (assoc-in m [:a-acceleration] (/ (first (:acceleration m)) 10.0)))

(defn update-angular-velocity [m]
  (-> (update-in m [:a-velocity] #(+ % (:a-acceleration m)))
      (update-in [:a-velocity] #(qc/constrain % -0.1 0.1))))

(defn update-angle [m]
  (update-in m [:angle] #(+ % (:a-velocity m))))

(defn update [m]
  (-> (update-velocity m)
      (update-location)
      (update-angular-acceleration)
      (update-angular-velocity)
      (update-angle)
      (assoc-in [:acceleration] [0.0 0.0])))

(defn loc-x [m]
  (first (:location m)))

(defn loc-y [m]
  (second (:location m)))

(defn display [m]
  (qc/stroke 255)
  (qc/fill 236 30 140 200)
  (qc/rect-mode :center)
  (qc/push-matrix)
  (qc/translate (loc-x m) (loc-y m))
  (qc/rotate (:angle m))
  (let [w (* 16.0 (:mass m))]
    (qc/stroke-weight 2)
    (qc/line 0 0 (/ w 2.0) 0)
    (qc/stroke-weight 0)
    (qc/rect 0 0 w w))
  (qc/pop-matrix))

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
                             (* -0.9 (vel-y m))])))

(defn check-edges [m width height]
  (let [m (cond (> (loc-x m) width) (bounce-x-right m width)
                (< (loc-x m) 0) (bounce-x-left m)
                :else m)]
    (if (> (loc-y m) height)
      (bounce-y-bottom m height)
      m)))
