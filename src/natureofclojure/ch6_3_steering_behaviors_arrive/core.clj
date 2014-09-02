;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://vimeo.com/channels/natureofcode/63089177
;;
(ns natureofclojure.ch6-3-steering-behaviors-arrive
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.math.fast-vector :as fvec]))

(def TARGET-R 100.0)
(def VEHICLE-R 5.0)
(def VEHICLES
  [{:location (fvec/fvec 20 20)
    :velocity (fvec/fvec 0 0)
    :acceleration (fvec/fvec 0 0)
    :max-speed 4
    :max-force 0.1}])

(defn setup []
  {:x 0 :y 0
   :vehicles VEHICLES})

(defn arrive-vehicle [vehicle]
  (let [target (fvec/fvec (q/mouse-x) (q/mouse-y)) 
        desired (fvec/- target (:location vehicle))
        d (fvec/magnitude desired)
        desired (fvec/normalize desired)
        desired (if (< d TARGET-R)
                  (fvec/* desired (q/map-range d 0 TARGET-R 0 (:max-speed vehicle)))
                  (fvec/* desired (:max-speed vehicle)))
        steer (fvec/- desired (:velocity vehicle))
        steer (fvec/limit steer (:max-force vehicle))]
    (-> vehicle
        (update-in [:acceleration] #(fvec/+ % steer)))))

(defn update-vehicle [vehicle]
  (let [v (fvec/+ (:velocity vehicle) (:acceleration vehicle))
        v (fvec/limit v (:max-speed vehicle))
        loc (fvec/+ v (:location vehicle))]
   (-> vehicle
       (assoc-in [:velocity] v)
       (assoc-in [:location] loc)
       (assoc-in [:acceleration] (fvec/fvec 0 0)))))

(defn update [state]
  (-> state
      (update-in [:vehicles] #(doall (map (comp update-vehicle
                                                arrive-vehicle) %)))))

(defn draw-vehicle
  [vehicle]
  (let [{:keys [location velocity]} vehicle
        [x y] (fvec/x-y location)
        theta (+ (/ Math/PI 2.0)
                 (fvec/heading velocity))]
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
