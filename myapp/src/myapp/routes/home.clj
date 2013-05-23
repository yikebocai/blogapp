(ns myapp.routes.home
  (:use compojure.core)
  (:require [myapp.views.layout :as layout]
            [myapp.util :as util]
            [myapp.models.loadblogs :as loadblogs]
            [myapp.models.gitpull :as gitpull]
            [myapp.models.db :as db]
            ))

(defn home-page []
  (layout/render
    "home.html" {:blogs (db/list-blog)}))

(defn about-page []
  (layout/render "about.html"))

(defn content-page [p]
  (layout/render
    "content.html" {:blog (loadblogs/load-blog-content p)}))

(defn sync-page []
  (layout/render 
    "sync.html"))

(defn sync-page-submit [path url]
  (layout/render 
    "sync.html" 
    {:path path 
     :url url  
     :result (gitpull/sync-blog path url)}))

(defn config-page []
  (layout/render 
    "config.html"))

(defn config-page-submit [path url]
  (layout/render 
    "config.html" 
    {:path path 
     :url url  
     ;:result (db/set-config path url)
   }))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/content" [p] (content-page p))
  (GET "/sync" [] (sync-page))
  (POST "/sync" [path url] (sync-page-submit path url))
  (GET "/config" [] (config-page))
  (POST "/config" [path url] (config-page-submit path url))
  (GET "/about" [] (about-page)))
