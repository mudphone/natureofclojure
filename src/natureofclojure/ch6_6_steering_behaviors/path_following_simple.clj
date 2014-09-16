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

(def PATH-R 20.0)

(def VEHICLE-R 5.0)

(defn random-vehicle [x y]
  {:location (fvec/fvec x y)
    :velocity (fvec/fvec 0.0 0.0)
    :acceleration (fvec/fvec 0.0 0.0)
    :max-speed (+ 4.0 (rand 4.0))
    :max-force (+ 0.2 (rand 0.2))})

(def VEHICLES
  [(random-vehicle (rand SIZE-W) (rand SIZE-H))])

(defn random-path []
  [(fvec/fvec 0.0    (rand SIZE-H))
   (fvec/fvec SIZE-W (rand SIZE-H))])

(def PATH (random-path))

(defn setup []
  {:vehicles VEHICLES
   :path PATH})

(defn update-vehicles [path vehicles]
  (doall (map #(->> %
                    (beh/follow path PATH-R)
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

(defn draw-path [path]
  (let [[a b] path
        [a-x a-y] (fvec/x-y a)
        [b-x b-y] (fvec/x-y b)]
    (q/no-stroke)
    (q/fill 200 255)
    (q/quad a-x (+ a-y PATH-R) a-x (- a-y PATH-R) b-x (- b-y PATH-R) b-x (+ b-y PATH-R))
    (q/stroke 100)
    (q/stroke-weight 1)
    (q/line a-x a-y b-x b-y)))

(defn draw [state]
  (q/background 255)
  (let [{:keys [path vehicles]} state]
    (q/fill 255)
    (draw-path path)
    (q/fill 255 0 0)
    (doall (map draw-vehicle vehicles))))

(defn key-pressed [state event]
  (assoc-in state [:path] (random-path)))

(defn mouse-pressed [state event]
  (let [{:keys [x y]} event]
    (update-in state [:vehicles] #(conj % (random-vehicle x y)))))

(q/defsketch quil-workflow
  :title "Steering Behaviors: Simple Path Following"
  :size [SIZE-W SIZE-H]
  :setup setup
  :update update
  :draw draw
  :key-pressed key-pressed
  :mouse-pressed mouse-pressed
  :middleware [m/fun-mode])
