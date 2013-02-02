(ns natureofclojure.intro-1.random-walker
  (:use [quil.core :as qc]))

(def WIDTH 800)
(def HEIGHT 600)
(def walker (atom {:x (/ WIDTH 2.0)
                   :y (/ HEIGHT 2.0)}))

(defn step-walker [w-atom]
  (let [choice (rand-int 4)]
    (cond (= choice 0) (swap! w-atom update-in [:x] #(qc/constrain-float (+ % 1) 0 WIDTH)))
    (cond (= choice 1) (swap! w-atom update-in [:x] #(qc/constrain-float (- % 1) 0 WIDTH)))
    (cond (= choice 2) (swap! w-atom update-in [:y] #(qc/constrain-float (+ % 1) 0 HEIGHT)))
    (cond (= choice 3) (swap! w-atom update-in [:y] #(qc/constrain-float (- % 1) 0 HEIGHT)))))

(defn render-walker-at-position [position]
  (stroke 0)
  (point (:x position) (:y position)))

(defn setup []
  (frame-rate 60)
  (background 255)
  (smooth))

(defn draw []
  (step-walker walker)
  (render-walker-at-position @walker))

(defsketch random-walker
  :title "Random Walker"
  :setup setup
  :draw draw
  :size [WIDTH HEIGHT])