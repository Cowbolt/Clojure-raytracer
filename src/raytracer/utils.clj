(ns raytracer.utils)

(defn dot
  "Dot product of vectors a and b"
  [a b]
  (reduce + (map * a b)))

(defn norm 
  "Norm of vector"
  [a]
  (Math/sqrt (reduce + (map #(Math/pow % 2) a))))

(defn normalize
  "Normalize vector"
  [a]
  (map #(/ % (norm a)) a))
