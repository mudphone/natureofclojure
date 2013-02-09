;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.math.vector
  (:require [clojure.math.numeric-tower :as math]))

(defn subtract [& vs]
  (apply map - vs))

(defn magnitude [v]
 (math/sqrt (reduce + (map #(math/expt % 2) v))))

(defn normalize [v]
  (let [m (magnitude v)]
    (vec (map #(/ % m) v))))

(defn multiply [scalar v]
  (vec (map * (repeat scalar) v)))