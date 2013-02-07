;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.ch1-2-pvector-class.ball.clojure-vector-ball
  (:require [quil.core :as qc]))

(def WIDTH 200.0)
(def HEIGHT 200.0)
(def ball (atom {:location [100 100]
                 :velocity [2.5 2.0]}))

(defn move [b-atom]
  (swap! b-atom assoc-in [:location] (map +
                                          (:location @b-atom)
                                          (:velocity @b-atom))))

(defn bounce [b-atom]
  (let [location (:location @b-atom)
        velocity (:velocity @b-atom)]
    (when (or (> (first location) WIDTH)
              (< (first location) 0))
      (swap! b-atom update-in [:velocity] #(map * % [-1 1])))
    (when (or (> (second location) HEIGHT)
              (< (second location) 0))
      (swap! b-atom update-in [:velocity] #(map * % [1 -1])))))

(defn display [b]
  ;; Display circle at x, y location
  (qc/stroke 0)
  (qc/stroke-weight 2)
  (qc/fill 127)
  (qc/ellipse (first (:location b))
              (second (:location b))
              48
              48))