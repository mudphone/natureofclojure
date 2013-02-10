;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.ch2-1-what-is-a-force.mover.mouse-acceleration
  (:require [quil.core :as qc])
  (:use [natureofclojure.math.vector :as mv]))

(def mover (atom {:location []
                  :velocity []
                  :top-speed 5.0}))

(defn init-mover [width height m]
  (-> (assoc-in m [:location] [(/ width 2.0) (/ height 2.0)])
      (assoc-in [:velocity] [0.0 0.0])))

(defn initialize [m-atom width height]
  (swap! m-atom (partial init-mover width height)))

(defn update-mover [m]
  (let [mouse [(qc/mouse-x) (qc/mouse-y)]
        acc (mv/subtract mouse (:location m))
        acc (mv/set-magnitude 0.1 acc)]
   (-> (update-in m [:velocity] #(mv/add % acc))
       (update-in [:location] #(mv/add % (:velocity m)))
       (update-in [:velocity] #(mv/limit (:top-speed m) %)))))

(defn update [m-atom]
  (swap! m-atom update-mover))

(defn display [m]
  (qc/stroke 0)
  (qc/stroke-weight 2)
  (qc/fill 127)
  (apply #(qc/ellipse %1 %2 48 48) (:location m)))

(defn check-edges [m-atom width height]
  (let [loc (:location @m-atom)
        x (first loc)
        y (second loc)]
    (cond (> x width) (swap! m-atom assoc-in [:location] [0 y])
          (< x 0) (swap! m-atom assoc-in [:location] [width y])
          (> y height) (swap! m-atom assoc-in [:location] [x 0])
          (< y 0) (swap! m-atom assoc-in [:location] [x height]))))