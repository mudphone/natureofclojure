;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://vimeo.com/channels/natureofcode/63101109
;;
(ns natureofclojure.ch6-4-flow-field-following
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.math.fast-vector :as fvec]))

(def RESOLUTION 10)
(def FLOW-SCALE (- RESOLUTION 2))
(def T 0.0)
(def T-OFFSET 0.05)
(def XY-OFFSET 0.1)

(defn f-theta [x y t]
  (let [x-off (* XY-OFFSET x)
        y-off (* XY-OFFSET y)]
    (q/map-range (q/noise x-off y-off t)
                 0.0 1.0
                 0.0 q/TWO-PI)))

(defn f-vec [x y t]
  (fvec/from-angle (f-theta x y t)))

(defn init-flow-field []
  (into {} (for [x (range (quot (q/width) RESOLUTION))
                 y (range (quot (q/height) RESOLUTION))]
             [[x y] (f-vec x y T)])))

(defn setup []
  {:time T
   :flow (init-flow-field)})

(defn update-flow-field [t flow]
  (let [update-vec (fn [[[x y] v]]
                     [[x y] (f-vec x y t)])]
    (into {} (doall
              (map update-vec flow)))))

(defn update [state]
  (let [{:keys [time]} state
        time (+ T-OFFSET time)]
    (-> state
        (assoc-in [:time] time)
        (update-in [:flow] (partial update-flow-field time)))))

(defn draw-flow-field [flow]
  (doseq [[key v] flow]
    (let [[x y] (map #(+ (/ RESOLUTION 2.0) (* RESOLUTION %)) key)
          len (* FLOW-SCALE (fvec/magnitude v))]
      (q/with-translation [x y]
        (q/with-rotation [(fvec/heading v)]
          (q/line 0 0 len 0))))))

(defn draw [state]
  (q/background 0)
  (q/stroke 255 55 141 255)
  (q/stroke-weight 0.5)
  (draw-flow-field (:flow state)))

(q/defsketch quil-workflow
  :title "Steering Behavior: Flow Field Following"
  :size [800 600]
  :setup setup
  :update update
  :draw draw
  :middleware [m/fun-mode])
