(ns raytracer.core
  (:import (javax.swing JFrame JPanel Timer)
           (java.awt Color Dimension)
           (java.awt.image BufferedImage)
           (java.awt.event ActionListener KeyListener KeyEvent))
  (:require [raytracer.render :as render]
            [raytracer.scene :refer [scene]])
  (:gen-class))

(declare paint im-panel)
(def width 384)
(def height 384)
(def buf-im (BufferedImage. width height BufferedImage/TYPE_INT_ARGB))

; FIXME: I don't really know anything about how swing works, so there's
; a bunch of redundant stuff right now.
(defn im-panel
  [frame]
  (proxy [JPanel ActionListener KeyListener] []
    (paintComponent [g]
      (proxy-super paintComponent g)
      (def im (render/draw-frame scene width height))
      (paint g im))

    (getPreferredSize []
      (Dimension. width height))

    (actionPerformed [e]
      (.repaint this))

    (keyPressed [e])
    (keyReleased [e])
    (keyTyped [e])))

(defn paint [g im]
  (. buf-im (setRGB
              0 0 width height
              (int-array (mapv #(. % (getRGB)) im))
              0 width))
    (. g (drawImage buf-im 0 0 nil))
    (println "Frame written"))

(defn -main
  [& args]
  (let [frame (JFrame. "Raytracer")
        panel (im-panel frame)]
    (.setFocusable panel true)
    (.addKeyListener panel panel)

    (.add frame panel)
    (.pack frame)
    (.setDefaultCloseOperation frame JFrame/EXIT_ON_CLOSE)
    (.setVisible frame true)))

