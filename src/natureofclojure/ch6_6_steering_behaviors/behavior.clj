;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.ch6-6-steering-behaviors.behavior
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.math.fast-vector :as fvec]))

(defn steer [target vehicle]
  (let [{:keys [location max-force max-speed velocity]} vehicle
        desired (fvec/- target location)
        desired (fvec/* (fvec/normalize desired) max-speed)
        steer-f (fvec/limit (fvec/- desired velocity) max-force)]
    (update-in vehicle [:acceleration] #(fvec/+ % steer-f))))

(defn follow [path path-r vehicle]
  (let [;; Predict location in future
        predict (fvec/* (fvec/normalize (:velocity vehicle)) 50)
        predict-loc (fvec/+ (:location vehicle) predict)

        ;; Look at line segment
        [a b] path

        ;; Get normal point to that line
        normal-point (fvec/scalar-projection predict-loc a b)

        ;; Find target point a little ahead of the normal
        dir (fvec/* (fvec/normalize (fvec/- b a)) 10)
        target (fvec/+ dir normal-point)

        ;; Only steer if outside path radius
        d (fvec/distance predict-loc normal-point)]
    (if (> d path-r)
      (steer target vehicle)
      vehicle)))

(defn borders
  ([path-start path-end vehicle-r vehicle]
     "Assumes multiple path segments (at least two)."
     (let [{:keys [location]} vehicle
           [x y] (fvec/x-y location)
           [start-x start-y] (fvec/x-y path-start)
           [end-x end-y] (fvec/x-y path-end)]
       (if (> x (+ end-x vehicle-r))
         (assoc-in vehicle [:location]
                   (fvec/fvec (- start-x vehicle-r)
                              (+ start-y (- y end-y))))
         vehicle)))
  ([path vehicle-r vehicle]
     "Assumes there is only one path segment."
     (borders (first path) (last path) vehicle-r vehicle)))

(defn move-vehicle [vehicle]
  (let [v (fvec/+ (:velocity vehicle) (:acceleration vehicle))
        v (fvec/limit v (:max-speed vehicle))
        loc (fvec/+ v (:location vehicle))]
   (-> vehicle
       (assoc-in [:velocity] v)
       (assoc-in [:location] loc)
       (assoc-in [:acceleration] (fvec/fvec 0 0)))))
