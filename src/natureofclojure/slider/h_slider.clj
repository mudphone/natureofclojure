;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Based on:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/chp6_agents/flocking_sliders/scrollbar.pde
;;
(ns natureofclojure.slider.h-slider
  (:require
   [quil.core :as q]))

(defn slider
  [{:keys [x y w h loose] :or {w 200.0 h 20.0 loose 2.0}}]
  {:w w
   :h h
   :loose loose
   :x-pos x
   :y-pos (- y (/ h 2.0))
   :s-pos x 
   :new-s-pos x
   :s-pos-min x
   :s-pos-max (- (+ x w) h)
   :over? false
   :scrollbar? false
   :locked? false})

(defn over? [slider]
  (let [mx (q/mouse-x)
        my (q/mouse-y)]
    (and (> mx (:x-pos slider))
         (< mx (+ (:x-pos slider)
                  (:w slider)))
         (> my (:y-pos slider))
         (< my (+ (:y-pos slider)
                  (:h slider))))))

(defn constrain [x x-min x-max]
  (max x-min (min x x-max)))

(defn update [slider]
  (let [s slider
        o? (over? s)
        mp? (q/mouse-pressed?)
        s (assoc-in s [:over?] o?)

        s (cond-> s
                  (and mp? o?)
                  (->
                   (assoc-in [:scrollbar?] true)
                   (assoc-in [:locked?] true))

                  (not mp?)
                  (->
                   (assoc-in [:scrollbar?] false)
                   (assoc-in [:locked?] false)))
        
        s (cond-> s
                  (:locked? s)
                  (->
                   (as-> s
                         (assoc-in s [:new-s-pos] (constrain (- (q/mouse-x)
                                                                (/ (:h s)
                                                                   2.0))
                                                             (:s-pos-min s)
                                                             (:s-pos-max s))))))
        s (cond-> s
                  (> (Math/abs (- (:new-s-pos s)
                                  (:s-pos s)))
                     0)
                  (as-> s
                        (update-in s [:s-pos] #(+ %
                                                  (/ (- (:new-s-pos s)
                                                        %)
                                                     (:loose s))))))]
    s))

(defn set-pos
  "Set slider position as ratio from 0.0 to 1.0"
  [slider pos]
  (let [new-s-pos (+ (:x-pos slider)
                     (* pos (- (:s-pos-max slider)
                               (:s-pos-min slider))))]
   (-> slider
       (assoc-in [:s-pos] new-s-pos)
       (assoc-in [:new-s-pos] new-s-pos))))

(defn get-pos
  "Get slider position as ratio from 0.0 to 1.0"
  [slider]
  (/ (- (:s-pos slider)
        (:x-pos slider))
     (- (:s-pos-max slider)
        (:s-pos-min slider))))

(defn draw
  [slider]
  (q/push-style)
  (q/fill 255)
  (q/rect-mode :corner)
  (q/rect (:x-pos slider) (:y-pos slider)
          (:w slider) (:h slider))
  (if (or (:over? slider) (:locked? slider))
    (q/fill 153 102 0)
    (q/fill 102 102 102))
  (q/rect (:s-pos slider) (:y-pos slider)
          (:h slider) (:h slider))
  (q/pop-style))
