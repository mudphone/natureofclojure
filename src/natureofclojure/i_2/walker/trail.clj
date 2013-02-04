;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/introduction/RandomWalkTrail/Walker.pde
;;
(ns natureofclojure.i-2.walker.trail
  (:require [quil.core :as qc]))

(def WIDTH 200.0)
(def HEIGHT 200.0)
(def walker (atom {:location [(/ WIDTH 2.0)
                              (/ HEIGHT 2.0)]
                   :history []})) ;; history is vec of vecs [[x y]..]

(defn updated-walker [w]
  (let [vel [(- (rand 4) 2) (- (rand 4) 2)]
        loc (vec (map + (:location w) vel))
        loc [(qc/constrain (first loc) 0 (- WIDTH 1))
             (qc/constrain (second loc) 0 (- HEIGHT 1))]
        h (vec (take-last 1000 (conj (:history w) loc)))]
    (-> (update-in w [:location] (constantly loc))
        (update-in [:history] (constantly h)))))

(defn step-walker [w-atom]
  (swap! w-atom updated-walker))

(defn loc-x [w]
  (first (:location w)))

(defn loc-y [w]
  (second (:location w)))

(defn render-walker [w]
  (qc/stroke 0)
  (qc/fill 175)
  (qc/rect-mode :center)
  (qc/rect (loc-x w) (loc-y w) 16 16)

  (qc/begin-shape)
  (qc/stroke 0)
  (qc/no-fill)
  (dorun
   (map #(qc/vertex (first %)  (second %)) (:history w)))
  (qc/end-shape))