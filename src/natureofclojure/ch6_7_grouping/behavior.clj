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

(defn apply-force [vehicle f-vector]
  (update-in vehicle [:acceleration] #(fvec/+ % f-vector)))

(defn between-0 [upper-d d]
  (and (> d 0.0)
       (< d upper-d)))

(defn glom [glom-dist all vehicle]
  (let [{:keys [location max-force max-speed velocity]} vehicle
        dist-veh (doall
                  (->> all
                       (dist-vehicle location)
                       (filter (fn [[d _]] (between-0 glom-dist d)))))
        num-vehicles (count dist-veh)]
    (if (< 0 num-vehicles)
      (let [sum-loc (doall
                     (reduce (fn [sum [d v]]
                               (fvec/+ sum (:location v)))
                             (fvec/fvec 0 0) dist-veh))
            steer (-> sum-loc
                      (fvec/- location)
                      (fvec/normalize)
                      (fvec/* max-speed)
                      (fvec/- velocity)
                      (fvec/limit max-force))]
        (apply-force vehicle steer))
      vehicle)))

(defn separate [separation-dist all vehicle]
  (let [{:keys [location max-force max-speed velocity]} vehicle
        dist-veh (doall
                  (->> all
                       (dist-vehicle location)
                       (filter (fn [[d _]] (between-0 separation-dist d)))))
        num-vehicles (count dist-veh)]
    (if (< 0 num-vehicles)
      (let [sum-dir (doall
                     (reduce (fn [avg-dir [d v]]
                               (let [diff (-> (fvec/- location (:location v))
                                              (fvec/normalize)
                                              (fvec// d))]
                                 (fvec/+ avg-dir diff)))
                             (fvec/fvec 0 0) dist-veh))
            steer (-> sum-dir
                      (fvec/normalize)
                      (fvec/set-mag max-speed)
                      (fvec/- velocity)
                      (fvec/limit max-force))]
        (apply-force vehicle steer))
      vehicle)))

(defn align [neighbor-dist all vehicle]
  (let [{:keys [location max-force max-speed velocity]} vehicle
        dist-veh (doall
                  (->> all
                       (dist-vehicle location)
                       (filter (fn [[d _]] (between-0 neighbor-dist d)))))
        num-vehicles (count dist-veh)]
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
        (apply-force vehicle steer))
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
