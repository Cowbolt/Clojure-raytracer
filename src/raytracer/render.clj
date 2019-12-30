(ns raytracer.render
  (:import (java.awt Graphics)
           (java.awt Color Dimension)
           (java.awt.image BufferedImage))
  (:require [raytracer.utils :as utils]))

(declare scene-intersect ray-sphere-intersect phong-lighting)
(defrecord Ray [origin direction])
(def fov (/ (. Math PI) 3))
(def background (Color. 0.2 0.7 0.8))

(defn reflect [I N]
  (map - I (map #(* % 2 (utils/dot I N)) N)))

; TODO: Blinn-Phong
(defn phong-lighting [scene point normal dir mat]
  (loop [dif-tot 0
         spec-tot 0
         lights (:lights scene)]
    (if (empty? lights)
      (+ (* dif-tot (:dif mat)) (* spec-tot (:spec mat)))

      (let [light (first lights)
            light-dir (utils/normalize (map - (:position light) point))
            light-dist (utils/norm (map - (:position light) point))]

        ; Check if light is blocked by any other elements in our scene. If so, skip it.
        (if (> (first (scene-intersect scene (->Ray point light-dir))) 0)
          (recur dif-tot spec-tot (rest lights))

          (let [dif-l (max 0 (* (utils/dot light-dir normal) (:intensity light)))
                spec-l (* (Math/pow (max 0 (utils/dot (reflect light-dir normal) dir)) 64) (:intensity light))]
            (recur (+ dif-tot dif-l) (+ spec-tot spec-l) (rest lights))))))))

(defn ray-sphere-intersect
  "Returns closest intersection of ray to sphere.
  Negative returns mean the intersection is invalid"
  [sphere ray]
  (let [oc (map - (:origin ray) (:center sphere))
        dir (:direction ray)
        radius (:radius sphere)
        a (utils/dot dir dir)
        b (* 2 (utils/dot oc dir))
        c (- (utils/dot oc oc) (* radius radius))
        discriminant (- (* b b) (* 4 a c))]
    (if (neg? discriminant) ; No intersection
      -1.0
      (/ (- (- b) (Math/sqrt discriminant)) (* 2 a)) ; Gets the closest intersect
      )))

(defn scene-intersect
  "Returns distance, surface point and normal, and material of closest scene intersection."
  [scene ray]
  (let [sphere-dist-vec (->> (map #(ray-sphere-intersect % ray) (:spheres scene))
                             (map vector (:spheres scene))
                             (filter #(pos? (last %))))]
    (if (empty? sphere-dist-vec)
      [-1 0 0 [0 0 0]] ; No valid intersections
      (let [[sphere dist] (apply min-key #(min (last %)) sphere-dist-vec) ; Get closest intersection
            point (map #(* dist %) (:direction ray))
            normal (utils/normalize (map - point (:center sphere)))]
        [dist point normal (:material sphere)]))))

; a and b convert our coordinates from windowspace to canonical screenspace,
; in order to do perspective rendering.
(defn get-pixel-color [scene x y height width]
  (let [a (* (- (/ (* 2 (+ x 0.5)) width) 1) (Math/tan (/ fov 2)) (/ width height))
        b (* -1 (- (/ (* 2 (+ y 0.5)) width) 1) (Math/tan (/ fov 2)))
        ray (->Ray [0 0 0] (utils/normalize [a b -1]))
        [dist point normal mat] (scene-intersect scene ray)] 
    (if (< dist 0)
      background
      (let [l (phong-lighting scene point normal (:direction ray) mat)
            [r g b] (map #(float (min 1.0 (* l %))) (:color mat))]
        (Color. r g b)))))

(defn draw-frame [scene width height]
  (loop [x 0
         y 0
         im []]
    (if (= y height)
      im
      (if (= (inc x) width)
        (recur 0 (inc y) (conj im (get-pixel-color scene x y height width))); End of scanline
        (recur (inc x) y (conj im (get-pixel-color scene x y height width)))))))
