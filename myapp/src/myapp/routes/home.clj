(ns myapp.routes.home
  (:use compojure.core)
  (:require [myapp.views.layout :as layout]
            [myapp.util :as util]
            [myapp.models.loadblogs :as loadblogs]
            ))

(defn home-page []
  (layout/render
    "home.html" {:blogs (loadblogs/load-blog "/Users/zxb/work/blog/src")}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
