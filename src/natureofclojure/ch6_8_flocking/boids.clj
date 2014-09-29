;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/tree/master/chp6_agents/NOC_6_09_Flocking
;;
(ns natureofclojure.ch6-8-flocking.separation-seek
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.math.fast-vector :as fvec]
   [natureofclojure.ch6-8-flocking.behavior :as beh]))

(def SIZE-W 800.0)
(def SIZE-H 600.0)

(def VEHICLE-R 4.0)

(def SEPARATION-DIST 30)
(def NEIGHBOR-DIST 100)
(def GLOM-DIST 50)

(defn random-v-comp []
  (let [r (+ 20.0 (rand 40.0))]
    (if (> (rand 1.0) 0.5)
      (* -1 r)
      r)))

(defn random-vehicle
  ([]
     (random-vehicle (rand SIZE-W) (rand SIZE-H)))
  ([x y]
     {:location (fvec/fvec x y)
      :velocity (fvec/fvec (random-v-comp)
                           (random-v-comp))
      :acceleration (fvec/fvec 0.0 0.0)
      :max-speed 3.0
      :max-force 0.2}))

(def VEHICLES
  (vec
   (for [_ (range 10)]
     (random-vehicle))))

(defn setup []
  {:vehicles VEHICLES})

(defn flock [all vehicle]
  (let [sep-factor   1.5
        align-factor 1.0
        glom-factor  1.0
        sep-force (fvec/*
                   (beh/separate SEPARATION-DIST all vehicle)
                   sep-factor)
        align-force (fvec/*
                     (beh/align NEIGHBOR-DIST all vehicle)
                     align-factor)
        glom-force (fvec/*
                    (beh/glom GLOM-DIST all vehicle)
                    glom-factor)]
    (-> vehicle
        (beh/apply-force sep-force)
        (beh/apply-force align-force)
        (beh/apply-force glom-force))))

(defn update-vehicles [vehicles]
  (doall
   (map #(->> %
              (flock vehicles)
              (beh/move-vehicle)
              (beh/borders SIZE-W SIZE-H VEHICLE-R))
        vehicles)))

(defn update [{:keys [vehicles] :as state}]
  (-> state
      (update-in [:vehicles] update-vehicles)))

(defn draw-vehicle
  [{:keys [location velocity]}]
  (let [[x y] (fvec/x-y location)
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
  (let [{:keys [vehicles]} state]
    (q/stroke 0.5)
    (q/fill 180)
    (doall (map draw-vehicle vehicles))))

(defn mouse-dragged [state event]
  (let [{:keys [x y]} event]
    (update-in state [:vehicles] #(conj % (random-vehicle x y)))))

(q/defsketch quil-workflow
  :title "Flocking: Boids"
  :size [SIZE-W SIZE-H]
  :setup setup
  :update update
  :draw draw
  :mouse-dragged mouse-dragged
  :middleware [m/fun-mode])
