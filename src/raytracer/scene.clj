(ns raytracer.scene)

(defrecord Material [color dif spec])
(defrecord Sphere [center radius material])
(defrecord Light [position intensity])

(def apple-green (->Material [0.25 1.0 0] 0.4 0.2))
(def cherry-red (->Material [1.0 0 0.25] 0.4 0.6))
(def mustard-yellow (->Material [0.85 1.0 0] 0.4 0.4))

(def scene {:spheres [(->Sphere [-2 4 -7.5]
                                1
                                apple-green)

                      (->Sphere [0.5 0 -5.0]
                                1
                                cherry-red)

                      (->Sphere [-0.9 -0.9 -8.8]
                                3
                                mustard-yellow)

                      (->Sphere [1.5 2 -4.0]
                                1
                                cherry-red)]

            :lights [(->Light [-15 -10 30] 1.1)
                     (->Light [3 -40 14] 0.8)]})
