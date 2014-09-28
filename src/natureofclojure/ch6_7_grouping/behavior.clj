;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.ch6-7-grouping.behavior
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.math.fast-vector :as fvec]))

(defn dist-vehicle [loc vehicles]
  (mapv (fn [v]
          (let [d (fvec/distance (:location v) loc)]
            [d v]))
        vehicles))

(defn align [neighbor-dist all vehicle]
  (let [{:keys [location max-force max-speed velocity]} vehicle
        dist-veh (doall
                  (->> all
                       (dist-vehicle location)
                       (filter (fn [[d _]]
                                 (and (> d 0.0)
                                      (< d neighbor-dist))))))
        num-vehicles (count dist-veh)
        ]
    (if (< 0 num-vehicles)
      (let [sum-dir (doall
                     (reduce (fn [avg-dir [d v]]
                               (fvec/+ avg-dir (:velocity v)))
                             (fvec/fvec 0 0) dist-veh))
            steer (-> sum-dir
                  (fvec// num-vehicles)
                  (fvec/normalize)
                  (fvec/* max-speed)
                  (fvec/- velocity)
                  (fvec/limit max-force))]
        (update-in vehicle [:acceleration] #(fvec/+ % steer)))
      vehicle)))

(defn borders
  [edge-x edge-y vehicle-r vehicle]
  "Assumes multiple path segments (at least two)."
  (let [{:keys [location]} vehicle
        [x y] (fvec/x-y location)]
    (cond->
     vehicle
     (> x (+ edge-x vehicle-r))
     (assoc-in [:location]
               (fvec/fvec (- 0 vehicle-r) y))

     (< x (- 0 vehicle-r))
     (assoc-in [:location]
               (fvec/fvec (+ edge-x vehicle-r) y))

     (> y (+ edge-y vehicle-r))
     (assoc-in [:location]
               (fvec/fvec x (- 0 vehicle-r)))

     (< y (- 0 vehicle-r))
     (assoc-in [:location]
               (fvec/fvec x (+ edge-y vehicle-r))))))

(defn move-vehicle [vehicle]
  (let [v (fvec/+ (:velocity vehicle) (:acceleration vehicle))
        v (fvec/limit v (:max-speed vehicle))
        loc (fvec/+ v (:location vehicle))]
   (-> vehicle
       (assoc-in [:velocity] v)
       (assoc-in [:location] loc)
       (assoc-in [:acceleration] (fvec/fvec 0 0)))))
