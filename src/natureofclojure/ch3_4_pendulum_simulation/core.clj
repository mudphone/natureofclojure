;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/tree/master/chp3_oscillation/NOC_3_10_PendulumExample
;;
(ns natureofclojure.ch3-4-pendulum-simulation.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn angular-acc
  "Calculate angular acceleration of a body in gravity"
  [gravity radius angle]
  (* -1 (/ gravity radius) (Math/sin angle)))

(def G 0.4)
(def LEN 175)
(def START-ANGLE (/ Math/PI 4.0))
(def START-ANG-VEL 0.0)
(def DAMPING 0.995)

(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  {:len LEN
   :ang-vel START-ANG-VEL
   :angle START-ANGLE
   :g G
   :dragging false})

(defn update [state]
  (if (:dragging state)
    (let [origin-x (/ (q/width) 2.0)
          origin-y 0.0
          diff-x (- origin-x (q/mouse-x))
          diff-y (- origin-y (q/mouse-y))
          a (- (Math/atan2 (* -1.0 diff-y) diff-x)
               (/ Math/PI 2.0))]
      (-> state
          (assoc-in [:angle] a)))
    (let [{:keys [angle ang-vel g len]} state
          ang-acc (angular-acc g len angle)
          ang-vel (* DAMPING (+ ang-vel ang-acc))]
      (-> state
          (update-in [:ang-vel] (constantly ang-vel))
          (update-in [:angle] #(+ ang-vel %))))))

(defn draw-ball
  [x y]
  (q/stroke 255)
  (q/stroke-weight 2)
  (q/line 0, 0, x, y)
  (q/no-stroke)
  (q/fill 255 255 255)
  (q/ellipse x y 50 50))

(defn loc [len angle]
  [(* len (Math/sin angle))
   (* len (Math/cos angle))])

(defn draw [state]
  (q/background 0)
  (let [{:keys [len angle]} state
        [x y] (loc len angle)]
    (q/with-translation [(/ (q/width) 2)
                         0]
      (draw-ball x y))))

(defn mouse-pressed [state event]
  (let [{:keys [len angle]} state
        [x y] (loc len angle)
        d (q/dist (- (q/mouse-x) (/ (q/width) 2.0)) (q/mouse-y) x y)]
    (-> state
        (assoc-in [:dragging] (< d len)))))

(defn mouse-released [state event]
  (-> state
      (assoc-in [:ang-vel] 0.0)
      (assoc-in [:dragging] false)))

(q/defsketch quil-workflow
  :title "pendulum example"
  :size [800 600]
  :setup setup
  :update update
  :draw draw
  :mouse-pressed mouse-pressed
  :mouse-released mouse-released
  :middleware [m/fun-mode])
