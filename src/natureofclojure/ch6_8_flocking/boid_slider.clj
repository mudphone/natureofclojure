;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/tree/master/chp6_agents/NOC_6_09_Flocking
;;
(ns natureofclojure.ch6-8-flocking.boid-slider
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.math.fast-vector :as fvec]
   [natureofclojure.ch6-8-flocking.behavior :as beh]
   [natureofclojure.slider.h-slider :as slider]))

(def SIZE-W 800.0)
(def SIZE-H 600.0)

(def VEHICLE-R 4.0)

(def SEPARATION-FACTOR 1.5)
(def SEPARATION-DIST 30)
(def ALIGN-FACTOR 1.0)
(def NEIGHBOR-DIST 100)
(def GLOM-FACTOR 1.0)
(def GLOM-DIST 50)

(def SEP-FACTOR-MIN 0.0)
(def SEP-FACTOR-MAX 5.0)
(def SEP-DIST-MIN 0.0)
(def SEP-DIST-MAX 300.0)

(def ALIGN-FACTOR-MIN 0.0)
(def ALIGN-FACTOR-MAX 5.0)
(def NEIGHBOR-DIST-MIN 0.0)
(def NEIGHBOR-DIST-MAX 300.0)

(def GLOM-FACTOR-MIN 0.0)
(def GLOM-FACTOR-MAX 5.0)
(def GLOM-DIST-MIN 0.0)
(def GLOM-DIST-MAX 300.0)

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

(defn map-pos [v min-v max-v]
  (q/map-range v min-v max-v 0.0 1.0))

(defn gen-slider [n label pos]
  (let [y (* (inc n) 20)]
   (-> (slider/slider {:x 10 :y y :w 100 :h 10 :label label})
       (slider/set-pos pos))))

(def SLIDERS [(gen-slider 0 "separation force"
                          (map-pos SEPARATION-FACTOR SEP-FACTOR-MIN SEP-FACTOR-MAX))
              (gen-slider 1 "separation distance"
                          (map-pos SEPARATION-DIST SEP-DIST-MIN SEP-DIST-MAX))
              (gen-slider 2 "alignment force"
                          (map-pos ALIGN-FACTOR ALIGN-FACTOR-MIN ALIGN-FACTOR-MAX))
              (gen-slider 3 "neighbor distance"
                          (map-pos NEIGHBOR-DIST NEIGHBOR-DIST-MIN NEIGHBOR-DIST-MAX))
              (gen-slider 4 "glom force"
                          (map-pos GLOM-FACTOR GLOM-FACTOR-MIN GLOM-FACTOR-MAX))
              (gen-slider 5 "glom distance"
                          (map-pos GLOM-DIST GLOM-DIST-MIN GLOM-DIST-MAX))])

(defn setup []
  (-> {:vehicles VEHICLES
       :sliders SLIDERS
       :sep-factor    SEPARATION-FACTOR
       :sep-dist      SEPARATION-DIST
       :align-factor  ALIGN-FACTOR
       :neighbor-dist NEIGHBOR-DIST
       :glom-factor   GLOM-FACTOR
       :glom-dist     GLOM-DIST}))

(defn flock [state all vehicle]
  (let [{:keys [sep-factor   sep-dist
                align-factor neighbor-dist
                glom-factor  glom-dist]} state
        sep-force   (fvec/*
                     (beh/separate sep-dist all vehicle)
                     sep-factor)
        align-force (fvec/*
                     (beh/align neighbor-dist all vehicle)
                     align-factor)
        glom-force  (fvec/*
                     (beh/glom glom-dist all vehicle)
                     glom-factor)]
    (-> vehicle
        (beh/apply-force sep-force)
        (beh/apply-force align-force)
        (beh/apply-force glom-force))))

(defn update-vehicles [state vehicles]
  (doall
   (mapv #(->> %
               (flock state vehicles)
               (beh/move-vehicle)
               (beh/borders SIZE-W SIZE-H VEHICLE-R))
         vehicles)))

(defn update-sliders [sliders m-x m-y mouse-pressed?]
  (doall
   (mapv #(slider/update % m-x m-y mouse-pressed?)
         sliders)))

(defn map-factor [factor min-v max-v]
  (q/map-range factor 0.0 1.0 min-v max-v))

(defn update [{:keys [sliders vehicles] :as state}]
  (let [[m-x m-y] [(q/mouse-x) (q/mouse-y)]
        mouse-pressed? (q/mouse-pressed?)
        updated-sliders (update-sliders sliders m-x m-y mouse-pressed?)
        sep-v   (map-factor (slider/get-pos (nth updated-sliders 0))
                            SEP-FACTOR-MIN SEP-FACTOR-MAX)
        sep-d   (map-factor (slider/get-pos (nth updated-sliders 1))
                            SEP-DIST-MIN SEP-DIST-MAX)
        
        align-v    (map-factor (slider/get-pos (nth updated-sliders 2))
                               ALIGN-FACTOR-MIN ALIGN-FACTOR-MAX)
        neighbor-d (map-factor (slider/get-pos (nth updated-sliders 3))
                               NEIGHBOR-DIST-MIN NEIGHBOR-DIST-MAX)
        
        glom-v  (map-factor (slider/get-pos (nth updated-sliders 4))
                            GLOM-FACTOR-MIN GLOM-FACTOR-MAX)
        glom-d  (map-factor (slider/get-pos (nth updated-sliders 5))
                            GLOM-DIST-MIN GLOM-DIST-MAX)]
    (-> state
        (assoc-in [:sep-factor] sep-v)
        (assoc-in [:sep-dist] sep-d)
        (assoc-in [:align-factor] align-v)
        (assoc-in [:neighbor-dist] neighbor-d)
        (assoc-in [:glom-factor] glom-v)
        (assoc-in [:glom-dist] glom-d)
        (#(update-in % [:vehicles] (partial update-vehicles %)))
        (assoc-in [:sliders] updated-sliders))))

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
  (let [{:keys [sliders vehicles]} state]
    (doall (map (comp
                 slider/draw
                 slider/draw-slider-label)
                sliders))
    (q/stroke 0.5)
    (q/fill 180)
    (doall (map draw-vehicle vehicles))))

(defn is-over-sliders? [x y]
  (and (< x (/ SIZE-W 2.0))
       (< y (/ SIZE-H 2.0))))

(defn mouse-dragged [state event]
  (let [{:keys [x y]} event]
    (if-not (is-over-sliders? x y)
      (update-in state [:vehicles] #(conj % (random-vehicle x y)))
      state)))

(q/defsketch quil-workflow
  :title "Flocking: Boids"
  :size [SIZE-W SIZE-H]
  :setup setup
  :update update
  :draw draw
  :mouse-dragged mouse-dragged
  :middleware [m/fun-mode])
