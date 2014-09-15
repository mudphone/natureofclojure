;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/chp6_agents/Exercise_6_09_AngleBetween/Exercise_6_09_AngleBetween.pde
;;
(ns natureofclojure.ch6-5-dot-product.angle-between
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.math.fast-vector :as fv]))

(defn setup []
  {})1

(defn update [state]
  state)

(defn draw-vector [v loc scayl]
  (let [arrow-size 6.0
        [loc-x loc-y] (fv/x-y loc)]
    (q/with-translation [loc-x loc-y]
      (q/with-rotation [(fv/heading v)]
        (let [len (* (fv/magnitude v) scayl)]
          (q/line 0 0 len 0)
          (q/line len 0 (- len arrow-size) (/ arrow-size 2.0))
          (q/line len 0 (- len arrow-size) (/ arrow-size -2.0)))))))

(defn draw-text [v x-axis]
  (q/fill 0)
  (let [theta (fv/angle-between v x-axis)
        degrees (* theta (/ 180.0 Math/PI))]
    (q/text (str degrees " degrees\n" theta " radians") 10 160)))

(defn draw [state]
  (q/background 255)
  (q/stroke 0)
  (q/stroke-weight 2)
  (let [mouse-loc (fv/fvec (q/mouse-x) (q/mouse-y))
        center-loc (fv/fvec (/ (q/width) 2.0) (/ (q/height) 2.0))
        v (-> (fv/- mouse-loc center-loc)
              (fv/normalize)
              (fv/* 75.0))
        x-axis (fv/fvec 75.0 0.0)]
    (draw-vector v center-loc 1.0)
    (draw-vector x-axis center-loc 1.0)
    (draw-text v x-axis)))

 (q/defsketch quil-workflow
  :title "Vectors: The Dot Product"
  :size [640 360]
  :setup setup
  :update update
  :draw draw
  :middleware [m/fun-mode])
