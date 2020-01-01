(ns raytracer.scene)

(defrecord Material [color dif spec reflect])
(defrecord Sphere [center radius material])
(defrecord Light [position intensity])

(def apple-green (->Material [0.25 1.0 0] 0.4 0.2 0.6))
(def red-rubber (->Material [0.3 0.1 0.1] 0.9 0.1 0.0))
(def ivory (->Material [0.4 0.4 0.3] 0.6 0.3 0.0))

(def scene {:spheres [(->Sphere [-3 0 -16]
                                2
                                ivory)

                      (->Sphere [-1.0 -1.5 -12]
                                2
                                red-rubber)

                      (->Sphere [1.5 -0.5 -18]
                                3
                                red-rubber)

                      (->Sphere [7 5 -18]
                                4
                                ivory)]

            :lights [(->Light [-20 20 30] 1.1)
                     (->Light [30 50 -25] 1.8)
                     (->Light [30 20 30] 1.7)
                     ]})
