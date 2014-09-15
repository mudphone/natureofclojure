;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.math.fast-vector
  (:require
   [clojure.core.matrix :as matrix]
   [clojure.core.matrix.operators :as mop]))

(defn setup-vectorz []
  (matrix/set-current-implementation :vectorz))
(setup-vectorz)

;; Implementation Specific:
(defn magnitude-squared [v]
  (.magnitudeSquared v))

(defn magnitude [v]
  (.magnitude v))

(defn x-y [v]
  (.toList v))

(defn dot [a b]
  (matrix/dot a b))

(defn normalize [v]
  (matrix/normalise v))

(defn fvec [x y]
  (matrix/array [x y]))


;; Operators:

(defn + [a b]
  (mop/+ a b))

(defn - [a b]
  (mop/- a b))

(defn * [a b]
  (mop/* a b))


;; Convenience:

(defn angle-between [a b]
  (let [dot-product (dot a b)]
    (Math/acos (/ dot-product
                  (* (magnitude a) (magnitude b))))))

(defn from-angle [theta]
  (fvec (Math/cos theta) (Math/sin theta)))

(defn limit [v max-mag]
  (if (> (magnitude-squared v) (clojure.core/* max-mag max-mag))
    (* (normalize v) max-mag)
    v))

(defn heading [v]
  (let [[x y] (x-y v)]
   (* -1.0 (Math/atan2 (* -1.0 y) x))))
