(ns myapp.routes.home
  (:use [compojure.core]
        [noir.request]
        [ring.util.response :only [redirect]])
  (:require [myapp.views.layout :as layout]
            [myapp.util :as util]
            [myapp.models.blog :as blog]
            [myapp.models.sync :as synch]
            [myapp.models.config :as config]
            [myapp.models.login :as login]
            [myapp.models.dbmanager :as dbmanager]
            [myapp.models.feed :as feed]
            [myapp.models.search :as search]
            [compojure.route :as route]
            [noir.response :as resp]
            ))

(defn home-page []
  (layout/render "home.html"
    {:blogs (blog/list-blog-summary)}))

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
       :url (config/get-value "url")})
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
       :url (config/get-value "url")
       :period (config/get-value "period")
       :blogname (config/get-value "blogname")
       :email (config/get-value "email")
       :password (config/get-value "password")
       :nickname (config/get-value "nickname")
       })
    (redirect "/")))

(defn config-page-submit [path url period blogname email password nickname]
  (println "aaaaaa")
  (layout/render
    "config.html"
    {:path path
     :url url
     :period period
     :blogname blogname
     :email email
     :password password
     :nickname nickname
     :result (config/set-config path url period blogname email password nickname)
     }))

(defn login-page []
  (layout/render "login.html"))

(defn login-page-submit [username password]
  (let [islogin (login/signin username password)]
    (if (true? islogin)
      (redirect "/config")
      (layout/render "login.html"
        {:username username
         :loginfailed true})
      )))

(defn tag-page [p]
  (layout/render
    "tag.html"
    {
      :blogs (blog/list-blog-summary p)
      }))

(defn dbmanager-page []
  (layout/render "dbmanager.html"))

(defn dbmanager-page-submit [tablename type]
  (if (empty? tablename)
    (str "please input the table name")
    (case type
      "init" (layout/render "dbmanager.html" {:init-result (dbmanager/init-table tablename)})
      "show" (layout/render "dbmanager.html" {:rows (dbmanager/show-table tablename)})
      "error")))

(defn search-page-submit [keyword]
  (layout/render "search.html" {:resultset (search/search keyword)}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (POST "/" [] (home-page-submit))
  (GET "/ok" [] (str "ok"))
  (GET "/about" [] (about-page))
  (GET "/blog" [p] (blog-page p))
  (GET "/sync" [] (sync-page))
  (POST "/sync" [path url] (sync-page-submit path url))
  (GET "/config" [] (config-page))
  (POST "/config" [path url period blogname email password nickname] (config-page-submit path url period blogname email password nickname))
  (GET "/login" [] (login-page))
  (POST "/login" [username password] (login-page-submit username password))
  (GET "/tag" [p] (tag-page p))
  (GET "/dbmanager" [] (dbmanager-page))
  (POST "/dbmanager" [tablename type] (dbmanager-page-submit tablename type))
  (GET "/feed" [] (resp/content-type "application/xml; charset=utf-8" (feed/create-feeds)))
  (GET "/search" [p] (search-page-submit p))

  )
