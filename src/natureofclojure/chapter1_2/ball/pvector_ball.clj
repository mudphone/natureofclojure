;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.chapter1-2.ball.pvector-ball
  (:import [processing.core PVector])
  (:require [quil.core :as qc]))

(def WIDTH 200.0)
(def HEIGHT 200.0)
(def ball (atom {:location (new PVector 100 100)
                 :velocity (new PVector 2.5 2)}))

(defn move [b]
  (.add (:location b) (:velocity b)))

(defn bounce [b]
  (let [location (:location b)
        velocity (:velocity b)]
    (when (or (> (.x location) WIDTH)
              (< (.x location) 0))
      (set! (. velocity x) (* -1 (.x velocity))))
    (when (or (> (.y location) HEIGHT)
              (< (.y location) 0))
      (set! (. velocity y) (* -1 (.y velocity))))))

(defn display [b]
  ;; Display circle at x, y location
  (qc/stroke 0)
  (qc/stroke-weight 2)
  (qc/fill 127)
  (qc/ellipse (.x (:location b)) (.y (:location b)) 48 48))