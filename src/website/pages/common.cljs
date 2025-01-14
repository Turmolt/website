(ns website.pages.common
  (:require ["prismjs/prism.js" :as prism]
            [reagent.core :as r]
            [reagent.dom :as rdom]
            [quil.core :as q]
            [quil.middleware :as m]))

(def mobile (> 960 (.-width js/screen)))

(def width (.-innerWidth js/window))
(def height (.-innerHeight js/window))

(defn iframe
  [{:keys [src name]}]
  [:iframe {:class "frame"
            :src src
            :name name
            :scrolling :no
            :style {:border "0px none"
                    :margin-top 5
                    :margin-bottom 10}}])

(defn canvas
  [{id :id setup :setup update :update size :size draw :draw art :atom}]
  (r/create-class
   {:component-did-mount
    (fn [component]
      (let [node (rdom/dom-node component)]
        (reset! art (q/sketch
                   :id id
                   :host node
                   :setup setup
                   :update update
                   :size size
                   :draw draw
                   :middleware [m/fun-mode]))))
    :component-will-unmount
    #(q/with-sketch @art (q/exit))
    :render (fn [] [:div])}))

(defn code [features & text]
  (r/create-class
   {:component-did-mount
    (fn [component]
      (let [node (rdom/dom-node component)]
        (prism/highlightElement node)))
    :render (fn [] [:code {:class features
                           :style {:font-size 15.5}} text])}))

(defn link
  [link text]
  [:a {:href link 
       :class :link}
   text])