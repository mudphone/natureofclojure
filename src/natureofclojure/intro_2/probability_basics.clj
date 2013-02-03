(ns natureofclojure.intro-2.probability-basics
  (:require [quil.core :as qc]))

(def WIDTH 800)
(def HEIGHT 600)
(def walker (atom {:x (/ WIDTH 2.0)
                   :y (/ HEIGHT 2.0)}))

(defn pull-walker
  "Slightly altered stepper to pull to the right."
  [w-atom]
  (let [choice (rand-int 100)]
    (cond (< choice 40) (swap! w-atom update-in [:x] #(qc/constrain-float (+ % 1) 0 WIDTH))
          (< choice 60) (swap! w-atom update-in [:x] #(qc/constrain-float (- % 1) 0 WIDTH))
          (< choice 80) (swap! w-atom update-in [:y] #(qc/constrain-float (+ % 1) 0 HEIGHT))
          :else (swap! w-atom update-in [:y] #(qc/constrain-float (- % 1) 0 HEIGHT)))))

(defn render-walker-at-position [position]
  (qc/stroke 0)
  (qc/point (:x position) (:y position)))

(defn setup []
  (qc/frame-rate 60)
  (qc/background 255)
  (qc/smooth))

(defn draw []
  (pull-walker walker)
  (render-walker-at-position @walker))

(defn run-2-1 []
  (qc/defsketch another-walker
    :title "Pulled Walker"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))