;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.math.vector
  (:require [clojure.math.numeric-tower :as math]))

(defn add [& vs]
  (vec (apply map + vs)))

(defn subtract [& vs]
  (vec (apply map - vs)))

(defn magnitude [v]
 (math/sqrt (reduce + (map #(math/expt % 2) v))))

(defn normalize [v]
  (let [m (magnitude v)]
    (vec (map #(/ % m) v))))

(defn multiply [scalar v]
  (vec (map * (repeat scalar) v)))

(defn divide [v scalar]
  (vec (map / v (repeat scalar))))

(defn limit [upper v]
  (let [m (magnitude v)]
    (if (> m upper)
      (multiply upper (normalize v))
      v)))

(defn set-magnitude [mag v]
  (multiply mag (normalize v)))

(defn random-2d []
  (normalize [(- (rand 2) 1.0) (- (rand 2) 1.0)]))