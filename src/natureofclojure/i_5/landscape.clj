;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.i-5.landscape
  (:require [quil.core :as qc]))

(def WIDTH 800.0)
(def HEIGHT 200.0)

(def INITIAL-LANDSCAPE-WIDTH 800)
(def INITIAL-LANDSCAPE-HEIGHT 400)
(def INITIAL-Z-OFFSET 0.0)

(defn initial-z-landscape [cols rows z-value]
  (apply merge
            (doall
             (for [x (range cols)
                   y (range rows)]
               {[x y] z-value}))))

(def landscape
  (atom {:size-cell 20.0
         :width INITIAL-LANDSCAPE-WIDTH
         :height INITIAL-LANDSCAPE-HEIGHT
         :z-off INITIAL-Z-OFFSET
         :z (initial-z-landscape INITIAL-LANDSCAPE-WIDTH
                                 INITIAL-LANDSCAPE-HEIGHT
                                 0.0)}))

(defn landscape-cols [ls]
  (/ (:width ls) (:size-cell ls)))

(defn landscape-rows [ls]
  (/ (:height ls) (:size-cell ls)))

(defn calculate-z [cols rows z-off]
  (apply merge
            (doall
             (for [x (range cols)
                   y (range rows)
                   :let [x-off (* 0.01 x)
                         y-off (* 0.01 y)]]
               {[x y] (qc/map-range (qc/noise x-off y-off z-off) 0 1 -800 800)}))))

(defn update-landscape-z [ls]
  (let [z (calculate-z (:width ls)
                       (:height ls)
                       (:z-off ls))]
    (-> (assoc-in ls [:z] z)
        (update-in [:z-off] #(+ % 0.05)))))

(defn calculate! [ls-atom]
  (swap! ls-atom update-landscape-z))

(defn render [ls]
  (let [scl (:size-cell ls)
        w (:width ls)
        h (:height ls)
        z (:z ls)]
    (dorun
     (for [x (range (- w 1))
           y (range (- h 1))
           :let [fill-color (qc/map-range (z [x y]) -120 120 0 255)]]
       (do
         (qc/stroke 0)
         (qc/fill fill-color 100)
        
         (qc/push-matrix)
         (qc/begin-shape :quads)
         (qc/translate (- (* x scl) (/ w 2.0))
                       (- (* y scl) (/ h 2.0))
                       0)
         (qc/vertex 0     0 (z [     x        y]))
         (qc/vertex scl   0 (z [(inc x)       y]))
         (qc/vertex scl scl (z [(inc x) (inc y)]))
         (qc/vertex 0   scl (z [     x  (inc y)]))
         (qc/end-shape)
         (qc/pop-matrix))))))