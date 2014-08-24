;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/Processing/chp3_oscillation/NOC_3_06_simple_harmonic_motion/NOC_3_06_simple_harmonic_motion.pde
;;
(ns natureofclojure.ch3-3-simple-harmonic-motion.oscillating-objects
  (:require [quil.core :as qc]))

(def WIDTH  800.0)
(def HEIGHT 200.0)
(def NUM_OSC 10)

;; Oscillator is a map, that looks like this:
;; { :angle [x y] :velocity [x y] :amplitude [x y] }
(def oscillators (atom []))
(defn gen-osc
  []
  {:angle [0 0]
   :velocity [(- (rand 0.1) 0.05), (- (rand 0.1) 0.05)]
   :amplitude [(qc/random 20 (/ WIDTH 2.0)) (qc/random (/ HEIGHT 2.0))]})
(defn gen-all-osc
  []
  (vec
   (doall
    (for [i (range NUM_OSC)]
      (gen-osc)))))

(defn oscillate
  [osc]
  (let [velocity (:velocity osc)
        angle (:angle osc)
        new-angle (map + velocity angle)]
    (assoc-in osc [:angle] new-angle)))

(defn display-osc
  [osc]
  (let [angle (:angle osc)
        amplitude (:amplitude osc)
        x (* (Math/sin (first angle)) (first amplitude))
        y (* (Math/sin (second angle)) (second amplitude))]
    (qc/push-matrix)
    (qc/translate (/ WIDTH 2.0) (/ HEIGHT 2.0))
    (qc/stroke 0)
    (qc/stroke-weight 2)
    (qc/fill 127 127)
    (qc/line 0 0 x y)
    (qc/ellipse x y 32 32)
    (qc/pop-matrix)))

(defn setup []
  (qc/smooth)
  (swap! oscillators #(do
                        %
                        (gen-all-osc))))

(defn draw []
  (qc/background 255)
  (dorun
   (map #(-> %
             (oscillate)
             (display-osc)) @oscillators)))

(defn run []
  (qc/defsketch oscillating-objects
    :title "Oscillating Objects"
    :setup setup
    :draw draw
    :size [WIDTH HEIGHT]))
