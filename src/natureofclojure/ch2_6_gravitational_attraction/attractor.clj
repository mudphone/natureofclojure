;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp2_forces/NOC_2_6_attraction/Attractor.pde
;;
(ns natureofclojure.ch2-6-gravitational-attraction.attractor
  (:require [quil.core :as qc]
            [natureofclojure.math.vector :as mv]))

(defn init-attractor [location mass m]
  (-> (assoc-in m [:location] location)
      (assoc-in [:mass] mass)))

(defn attractor
  ([]
     {:location []
      :mass 20.0
      :g 1.0})
  ([location]
     (attractor location 1.0))
  ([location mass]
     (init-attractor location mass (attractor))))

(defn attract [a m]
  (let [force (mv/subtract (:location a) (:location m))
        d (mv/magnitude force)
        d (qc/constrain d 1.0 5.0)
        n (mv/normalize force)
        strength (/ (* (:g a) (:mass a) (:mass m)) (* d d))]
    (mv/multiply strength n)))

(defn display [a]
  (qc/ellipse-mode :center)
  (qc/stroke-weight 4)
  (qc/stroke 0)
  (qc/fill 127 200)
  (let [d (* 2.0 (:mass a))]
    (apply #(qc/ellipse %1 %2 d d) (:location a))))