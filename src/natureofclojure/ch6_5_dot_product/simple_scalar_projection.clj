;; Based on the Nature of Code
;; by Daniel Shiffman
;; http://natureofcode.com
;;
;; Specifically:
;; https://github.com/shiffman/The-Nature-of-Code-Examples/blob/master/chp6_agents/SimpleScalarProjection/SimpleScalarProjection.pde
;;
(ns natureofclojure.ch6-5-dot-product.simple-scalar-projection
  (:require
   [quil.core :as q]
   [quil.middleware :as m]
   [natureofclojure.math.fast-vector :as fv]))

(defn setup []
  {})

(defn update [state]
  state)

(defn scalar-projection [p a b]
  (let [ap (fv/- p a)
        ab (fv/- b a)
        ab-unit (fv/normalize ab)
        shadow (fv/* (fv/dot ap ab-unit) ab-unit)]
    (fv/+ a shadow)))

(defn draw [state]
  (q/background 255)
  (let [a (fv/fvec 20 300)
        [a-x a-y] (fv/x-y a)
        b (fv/fvec 500 250)
        [b-x b-y] (fv/x-y b)
        mouse (fv/fvec (q/mouse-x) (q/mouse-y))
        [m-x m-y] (fv/x-y mouse)]
    (q/stroke 0)
    (q/stroke-weight 2)
    (q/line a-x a-y b-x b-y)
    (q/line a-x a-y m-x m-y)
    (q/fill 0)
    (q/ellipse a-x a-y 8 8)
    (q/ellipse b-x b-y 8 8)
    (q/ellipse m-x m-y 8 8)
    (let [norm (scalar-projection mouse a b)
          [norm-x norm-y] (fv/x-y norm)]
      (q/stroke 50)
      (q/stroke-weight 1)
      (q/line m-x m-y norm-x norm-y)

      (q/no-stroke)
      (q/fill 255 0 0)
      (q/ellipse norm-x norm-y 16 16))))

 (q/defsketch quil-workflow
  :title "Vectors: The Dot Product"
  :size [640 360]
  :setup setup
  :update update
  :draw draw
  :middleware [m/fun-mode])
