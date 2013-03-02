;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp2_forces/NOC_2_8_mutual_attraction/NOC_2_8_mutual_attraction.pde
;;
(ns natureofclojure.ch2-6-gravitational-attraction.mutual-attraction
  (:require [quil.core :as qc]
            [natureofclojure.ch2-6-gravitational-attraction.mutual-mover :as mover]))

(def WIDTH 800.0)
(def HEIGHT 200.0)
(def movers (atom []))

(defn reset-movers! []
  (let [ms (vec
            (doall
             (for [i (range 20)
                   :let [mass (+ 0.1 (rand 1.9))]]
               (mover/mover [0.0 0.0] [(rand WIDTH) (rand HEIGHT)] mass))))]
    (swap! movers (constantly ms))))

(defn setup []
  (qc/smooth)
  (reset-movers!))

(defn attract-movers []
  (let [cnt (count @movers)
        g 0.4]
    (dorun
     (for [i (range cnt)
           j (range cnt)
           :when (not=  (int i) (int j))
           :let [m (nth @movers i)
                 f (mover/attract (nth @movers j) (nth @movers i) g)]]
       (let [mvrs (assoc-in @movers [i] (mover/apply-force m f))]
         (swap! movers (constantly mvrs)))))))

(defn draw []
  (qc/background 255)
  (attract-movers)
  (let [stepped-movers (vec
                        (doall
                         (map mover/update @movers)))]
    (dorun
     (map mover/display stepped-movers))
    (swap! movers (constantly stepped-movers))
    )
  )

(defn mouse-pressed []
  (reset-movers!))

(defn run []
  (qc/defsketch gravitational-attraction-mutual
    :title "Gravitational Attraction: Mutual"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]
    :mouse-pressed mouse-pressed))
