(ns myapp.routes.home
  (:use compojure.core)
  (:require [myapp.views.layout :as layout]
            [myapp.util :as util]
            [myapp.models.loadblogs :as loadblogs]
            ))

(defn home-page []
  (layout/render
    "home.html" {:blogs (loadblogs/load-blog-list "/Users/zxb/work/blog/src")}))

(defn about-page []
  (layout/render "about.html"))

(defn content-page []
  (layout/render
    "content.html" {:blog (loadblogs/load-blog-content "")}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/content" [] (content-page))
  (GET "/about" [] (about-page)))
