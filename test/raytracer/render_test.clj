(ns raytracer.render-test
  (:require [clojure.test :refer :all]
            [raytracer.render :refer :all]
            [raytracer.scene :refer [scene]]))

(deftest ray-sphere-intersect-test
  (testing "Valid intersection yields distance"
    (let [sphere (->Sphere [10 0 0] 3)
          ray (->Ray [0 0 0] [1 0 0])]
          (is (= (ray-sphere-intersect sphere ray) 7.0)))
    (let [sphere (->Sphere [250 250 200] 100)
          ray (->Ray [250 250 0] [0 0 1])]
          (is (= (ray-sphere-intersect sphere ray) 100.0))))

  (testing "No intersection yields -1"
    (let [sphere (->Sphere [10 10 0] 3)
          ray (->Ray [0 0 0] [1 0 0])]
          (is (= (ray-sphere-intersect sphere ray) -1.0)))
    (let [sphere (->Sphere [250 250 200] 100)
          ray (->Ray [0 0 0] [0 0 1])]
          (is (= (ray-sphere-intersect sphere ray) -1.0))))

  (testing "Overlapping intersection (positive and negative) yields negative distance"
    (let [sphere (->Sphere [0 0 0] 3)
          ray (->Ray [0 0 0] [1 0 0])]
          (is (neg? (ray-sphere-intersect sphere ray))))))

(deftest draw-frame-test
  (testing "Should return a vector of count width*height"
    (let [width 100
          height 100]
      (is (= (count (draw-frame scene width height)) (* width height))))))
