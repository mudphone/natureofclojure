;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.ch6-5-dot-product.behavior
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.math.fast-vector :as fvec]))

(defn arrive-vehicle [vehicle target target-r]
  (let [desired (fvec/- target (:location vehicle))
        d (fvec/magnitude desired)
        desired (fvec/normalize desired)
        desired (if (< d target-r)
                  (fvec/* desired (q/map-range d 0 target-r 0 (:max-speed vehicle)))
                  (fvec/* desired (:max-speed vehicle)))
        steer (fvec/- desired (:velocity vehicle))
        steer (fvec/limit steer (:max-force vehicle))]
    (-> vehicle
        (update-in [:acceleration] #(fvec/+ % steer)))))

(defn move-vehicle [vehicle]
  (let [v (fvec/+ (:velocity vehicle) (:acceleration vehicle))
        v (fvec/limit v (:max-speed vehicle))
        loc (fvec/+ v (:location vehicle))]
   (-> vehicle
       (assoc-in [:velocity] v)
       (assoc-in [:location] loc)
       (assoc-in [:acceleration] (fvec/fvec 0 0)))))
