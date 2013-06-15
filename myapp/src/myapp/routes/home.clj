(ns myapp.routes.home
  (:use [compojure.core]
        [noir.request]
        [ring.util.response :only [redirect]])
  (:require [myapp.views.layout :as layout]
            [myapp.util :as util]
            [myapp.models.blog :as blog]
            [myapp.models.sync :as synch]
            [myapp.models.db :as db]
            [myapp.models.config :as config]
            [myapp.models.login :as login]
            [compojure.route :as route]
            ))

(defn home-page []
  (layout/render "home.html" 
    {:blogs (db/list-blog)}))

(defn home-page-submit []
  (let [referer (util/get-header "referer")]
  (do 
    (login/signout)
    (redirect referer))))

(defn about-page []
  (layout/render "about.html"))

(defn blog-page [p]
  (layout/render
    "blog.html" {:blog (blog/load-blog-content p)}))

(defn sync-page []
  (if (util/islogin)
      (layout/render "sync.html"
        {:path (config/get-value "path")
        :url (config/get-value "url") })
      (redirect "/")
      ))

(defn sync-page-submit [path url]
  (let [result (synch/sync-blog path url)]
    (layout/render 
    "sync.html" 
    {:path path 
     :url url 
     :result result
     :error (.startsWith result "ERROR:")})))

(defn config-page []
  (if (util/islogin)
  (layout/render 
    "config.html"
    {:path (config/get-value "path")
     :url (config/get-value  "url") 
     :period (config/get-value "period")
     :blogname (config/get-value "blogname")
     :email (config/get-value "email")
     :password (config/get-value "password")
     :nickname (config/get-value "nickname") 
     })
  (redirect "/")))

(defn config-page-submit [path  url period blogname email password nickname]
  (layout/render 
    "config.html" 
    {:path path 
     :url url  
     
     :period period
     :blogname blogname
     :email email
     :password password
     :nickname nickname
     :result (config/set-config path  url period blogname email password nickname)
   }))

(defn login-page[] 
  (layout/render "login.html"))

(defn login-page-submit [username password]
  ( let [islogin (login/signin username password)]
    (if (true? islogin)
      (redirect "/config")
      (layout/render "login.html"
        {:username username 
          :loginfailed true})
      )))

;;app startup check
(defn ok-page []
  (layout/render "ok.html"))

(defn tag-page [p]
  (layout/render "tag.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/" [] (home-page-submit))
  (GET "/ok" [] (ok-page))
  (GET "/about" [] (about-page))
  (GET "/blog" [p] (blog-page p))
  (GET "/sync" [] (sync-page))
  (POST "/sync" [path url] (sync-page-submit path url))
  (GET "/config" [] (config-page))
  (POST "/config" [path url period blogname email password nickname] (config-page-submit path url period blogname email password nickname))
  (GET "/login" [] (login-page))
  (POST "/login" [username password] (login-page-submit username password)) 
  (GET "/tag" [p] (tag-page p))
  )
