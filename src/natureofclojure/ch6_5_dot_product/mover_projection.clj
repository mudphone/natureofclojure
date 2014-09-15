;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; 
;;
(ns natureofclojure.ch6-5-dot-product.mover-projection
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.math.fast-vector :as fvec]
   [natureofclojure.ch6-5-dot-product.behavior :as beh]))

(def TARGET-R 100.0)
(def VEHICLE-R 5.0)
(def VEHICLES
  [{:location (fvec/fvec 20 20)
    :velocity (fvec/fvec 0 0)
    :acceleration (fvec/fvec 0 0)
    :max-speed 4
    :max-force 0.1}
   {:location (fvec/fvec 80 100)
    :velocity (fvec/fvec 0 0)
    :acceleration (fvec/fvec 0 0)
    :max-speed 8
    :max-force 0.2}])

(defn setup []
  {:x 0 :y 0
   :vehicles VEHICLES})

(defn update-vehicles [vehicles]
  (let [target (fvec/fvec (q/mouse-x) (q/mouse-y))]
    (doall (map #(-> %
                     (beh/arrive-vehicle target TARGET-R)
                     (beh/move-vehicle))
                vehicles))))

(defn update [state]
  (-> state
      (update-in [:vehicles] update-vehicles)))

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

(defn scalar-projection [p a b]
  (let [ap (fvec/- p a)
        ab (fvec/- b a)
        ab-unit (fvec/normalize ab)
        shadow (fvec/* (fvec/dot ap ab-unit) ab-unit)]
    (fvec/+ a shadow)))

(defn draw-vehicle-projection [a b vehicle]
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

(defn draw-projections [vehicles]
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

(defn draw [state]
  (q/background 0)
  (let [{:keys [x y vehicles]} state]
    (q/fill 255)
    (q/with-translation [x y]
      (q/ellipse 0 0 20 20))
    (q/fill 255 0 0)
    (doall (map draw-vehicle vehicles))
    (draw-projections vehicles)))

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
