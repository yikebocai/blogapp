(ns myapp.routes.home
  (:use compojure.core)
  (:require [myapp.views.layout :as layout]
            [myapp.util :as util]
            [myapp.models.loadblogs :as loadblogs]
            ))

(defn home-page []
  (layout/render
    "home.html" {:blogs (loadblogs/load-blog-list "/Users/zxb/work/blog/src/")}))

(defn about-page []
  (layout/render "about.html"))

(defn content-page [p]
  (layout/render
    "content.html" {:blog (loadblogs/load-blog-content p)}))

(defn sync-page []
  (layout/render 
    "sync.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/content" [p] (content-page p))
  (GET "/sync" [] (sync-page))
  (GET "/about" [] (about-page)))
