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
  {:sliders [(slider/slider {:x 10 :y 10 :label "Slider A"})
             (slider/slider {:x 10 :y 100 :label "Slider B"})]})

(defn update [state]
  (-> state
      (update-in [:sliders] #(doall
                              (mapv slider/update %)))))

(defn draw-slider-label [slider]
  (let [{:keys [x-pos y-pos w h label]} slider
        v (slider/get-pos slider)
        spacing 4.0]
    (q/push-style)
    (q/fill 255)
    (q/text-align :left)
    (q/text (str label ": " (format "%.3f" v))
            (+ x-pos w spacing)
            (+ y-pos h))
    (q/pop-style)))

(defn draw [{:keys [sliders]}]
  (q/background 0)
  (dorun
   (map slider/draw sliders))
  (dorun
   (map draw-slider-label sliders)))

(q/defsketch quil-workflow
  :title "Slider Demo"
  :size [SIZE-W SIZE-H]
  :setup setup
  :update update
  :draw draw
  :middleware [m/fun-mode])
