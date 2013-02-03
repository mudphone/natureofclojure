(ns natureofclojure.i-3.random-distribution
  (:require [quil.core :as qc]))

(def WIDTH 800)
(def HEIGHT 200)
(def random-counts (atom (vec (take 20 (cycle [0])))))

(defn setup [])

(defn draw
  []
  (qc/background 255)

  ;; Pick a random number and increase the count
  (let [index (rand-int (count @random-counts))]
    (swap! random-counts update-in [index] inc))

  ;; Draw a rectangle to graph results
  (qc/stroke 0)
  (qc/stroke-weight 2)
  (qc/fill 127)

  (dorun
   (let [w (/ WIDTH (count @random-counts))
         draw-rect (fn [x c]
                     (qc/rect (* x w)
                              (- HEIGHT c)
                              (- w 1)
                              c))]
     (map-indexed draw-rect @random-counts))))

(defn run []
  (qc/defsketch random-distribution
    :title "Random Distribution"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))