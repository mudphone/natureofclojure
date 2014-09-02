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

(defn limit [v max-mag]
  (if (> (magnitude-squared v) (clojure.core/* max-mag max-mag))
    (* (normalize v) max-mag)
    v))

(defn heading [v]
  (let [[x y] (x-y v)]
   (* -1.0 (Math/atan2 (* -1.0 y) x))))
