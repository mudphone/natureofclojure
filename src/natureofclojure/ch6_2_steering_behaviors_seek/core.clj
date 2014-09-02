;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://vimeo.com/channels/natureofcode/63089177
;;
(ns natureofclojure.ch6-2-steering-behaviors-seek
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.core.matrix :as matrix]
            [clojure.core.matrix.operators :as mop]))

(matrix/set-current-implementation :vectorz)

(def VEHICLE-R 5.0)
(def VEHICLES
  [{:location (matrix/array [20 20])
    :velocity (matrix/array [0 0])
    :acceleration (matrix/array [0 0])
    :max-speed 4
    :max-force 0.1}])

(defn setup []
  {:x 0 :y 0
   :vehicles VEHICLES})

(defn limit [vec max-mag]
  (if (> (.magnitudeSquared vec) (* max-mag max-mag))
    (mop/* (matrix/normalise vec) max-mag)
    vec))

(defn heading [vec]
  (let [[x y] (.toList vec)]
   (* -1.0 (Math/atan2 (* -1.0 y) x))))

(defn seek-vehicle [vehicle]
  (let [target (matrix/array [(q/mouse-x) (q/mouse-y)]) 
        desired (mop/- target (:location vehicle))
        desired (matrix/normalise desired)
        desired (mop/* desired (:max-speed vehicle))
        steer (mop/- desired (:velocity vehicle))
        steer (limit steer (:max-force vehicle))]
    (-> vehicle
        (update-in [:acceleration] #(mop/+ % steer)))))

(defn update-vehicle [vehicle]
  (let [vel (mop/+ (:velocity vehicle) (:acceleration vehicle))
        vel (limit vel (:max-speed vehicle))
        loc (mop/+ vel (:location vehicle))]
   (-> vehicle
       (assoc-in [:velocity] vel)
       (assoc-in [:location] loc)
       (assoc-in [:acceleration] (matrix/array [0 0])))))

(defn update [state]
  (-> state
      (update-in [:vehicles] #(doall (map (comp update-vehicle
                                                seek-vehicle) %)))))

(defn draw-vehicle
  [vehicle]
  (let [{:keys [location velocity]} vehicle
        [x y] (.toList location)
        [vx vy] (.toList velocity)
        theta (+ (/ Math/PI 2.0)
                 (heading (matrix/array [vx vy])))]
    (q/with-translation [x y]
      (q/with-rotation [theta]
        (q/begin-shape)
        (q/vertex 0                  (* -2.0 VEHICLE-R))
        (q/vertex (* -1.0 VEHICLE-R) (* 2.0 VEHICLE-R))
        (q/vertex VEHICLE-R          (* 2.0 VEHICLE-R))
        (q/end-shape :close)))))

(defn draw [state]
  (q/background 0)
  (let [{:keys [x y]} state]
    (q/fill 255)
    (q/with-translation [x y]
      (q/ellipse 0 0 20 20))
    (q/fill 255 0 0)
    (doall (map draw-vehicle (:vehicles state)))))

(defn mouse-moved [state event]
  (-> state
      (assoc-in [:x] (:x event))
      (assoc-in [:y] (:y event))))

(q/defsketch quil-workflow
  :title "Steering Behavior: Seek"
  :size [800 600]
  :setup setup
  :update update
  :draw draw
  :mouse-moved mouse-moved
  :middleware [m/fun-mode])
