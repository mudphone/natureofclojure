;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; 
;;
(ns natureofclojure.ch6-6-steering-behaviors.path-following-simple
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.math.fast-vector :as fvec]
   [natureofclojure.ch6-6-steering-behaviors.behavior :as beh]))

(def SIZE-W 800.0)
(def SIZE-H 600.0)

(def VEHICLE-R 5.0)
(def VEHICLES
  [{:location (fvec/fvec 20.0 20.0)
    :velocity (fvec/fvec 0.0 0.0)
    :acceleration (fvec/fvec 0.0 0.0)
    :max-speed 4.0
    :max-force 0.1}])

(defn random-path []
  [(fvec/fvec 0.0    (rand SIZE-H))
   (fvec/fvec SIZE-W (rand SIZE-H))])

(def PATH (random-path))

(defn setup []
  {:vehicles VEHICLES
   :path PATH})

(defn update-vehicles [path vehicles]
  (doall (map #(->> %
                    (beh/follow path)
                    (beh/move-vehicle)
                    (beh/borders path VEHICLE-R))
              vehicles)))

(defn update [{:keys [vehicles path] :as state}]
  (-> state
      (update-in [:vehicles] (partial update-vehicles path))))

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

#_(defn draw-vehicle-projection [a b vehicle]
  (let [[a-x a-y] (fvec/x-y a)
        mouse (:location vehicle)
        [m-x m-y] (fvec/x-y mouse)]
    (q/stroke 255)
    (q/line a-x a-y m-x m-y)
    (let [norm (scalar-projection mouse a b)
          [norm-x norm-y] (fvec/x-y norm)]
      (q/stroke 50)
      (q/stroke-weight 1)
      (q/line m-x m-y norm-x norm-y)

      (q/no-stroke)
      (q/fill 255 0 0)
      (q/ellipse norm-x norm-y 16 16))))

#_(defn draw-projections [vehicles]
  (let [a (fvec/fvec 20 (- (q/height) 20))
        [a-x a-y] (fvec/x-y a)
        b (fvec/fvec (- (q/width) 50) (- (q/height) 70))
        [b-x b-y] (fvec/x-y b)]
    (q/stroke 255)
    (q/stroke-weight 1)
    (q/line a-x a-y b-x b-y)
    (q/fill 255)
    (q/ellipse a-x a-y 8 8)
    (q/ellipse b-x b-y 8 8)
    (dorun
     (map (partial draw-vehicle-projection a b) vehicles))))

(defn draw-path [path]
  (let [[a b] path
        [a-x a-y] (fvec/x-y a)
        [b-x b-y] (fvec/x-y b)]
    (q/stroke 0)
    (q/stroke-weight 2)
    (q/line a-x a-y b-x b-y)))

(defn draw [state]
  (q/background 255)
  (let [{:keys [path vehicles]} state]
    (q/fill 255)
    (draw-path path)
    (q/fill 255 0 0)
    (doall (map draw-vehicle vehicles))
    #_(draw-projections vehicles)
    ))

(defn key-pressed [state event]
  (assoc-in state [:path] (random-path)))

(q/defsketch quil-workflow
  :title "Steering Behaviors: Simple Path Following"
  :size [SIZE-W SIZE-H]
  :setup setup
  :update update
  :draw draw
  :key-pressed key-pressed
  :middleware [m/fun-mode])
