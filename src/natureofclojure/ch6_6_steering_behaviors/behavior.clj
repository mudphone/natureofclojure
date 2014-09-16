;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.ch6-6-steering-behaviors.behavior
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.math.fast-vector :as fvec]))

(def PATH-R 20.0)

(defn steer [target vehicle]
  (let [{:keys [location max-force max-speed velocity]} vehicle
        desired (fvec/- target location)
        desired (fvec/* (fvec/normalize desired) max-speed)
        steer-f (fvec/limit (fvec/- desired velocity) max-force)]
    (update-in vehicle [:acceleration] #(fvec/+ % steer-f))))

(defn follow [path vehicle]
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
    (if (> d PATH-R)
      (steer target vehicle)
      vehicle)))

(defn borders [path vehicle-r vehicle]
  (let [{:keys [location]} vehicle
        [x y] (fvec/x-y location)
        [path-start path-end] path
        [start-x start-y] (fvec/x-y path-start)
        [end-x end-y] (fvec/x-y path-end)]
    (if (> x (+ end-x vehicle-r))
      (assoc-in vehicle [:location] (fvec/fvec (- start-x vehicle-r)
                                               (+ start-y (- y end-y))))
      vehicle)))

(defn move-vehicle [vehicle]
  (let [v (fvec/+ (:velocity vehicle) (:acceleration vehicle))
        v (fvec/limit v (:max-speed vehicle))
        loc (fvec/+ v (:location vehicle))]
   (-> vehicle
       (assoc-in [:velocity] v)
       (assoc-in [:location] loc)
       (assoc-in [:acceleration] (fvec/fvec 0 0)))))
