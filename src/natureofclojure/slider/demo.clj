;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
(ns natureofclojure.slider.demo
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.slider.h-slider :as slider]))

(def SIZE-W 800.0)
(def SIZE-H 600.0)

(defn setup []
  {:sliders [(slider/slider {:x 10 :y 10})
             (slider/slider {:x 10 :y 100})]})

(defn update [state]
  (-> state
      (update-in [:sliders] #(doall
                              (mapv slider/update %)))))

(defn draw [state]
  (q/background 0)
  (dorun
   (map slider/draw (:sliders state))))

(q/defsketch quil-workflow
  :title "Slider Demo"
  :size [SIZE-W SIZE-H]
  :setup setup
  :update update
  :draw draw
  :middleware [m/fun-mode])
