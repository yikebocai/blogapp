(ns myapp.handler
  (:use myapp.routes.home
        compojure.core)
  (:require [noir.util.middleware :as middleware]
            [compojure.route :as route]
            [taoensso.timbre :as timbre] 
            [myapp.models.schema :as schema]
            [myapp.middleware :as mymiddleware]
            ))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []

  ;;init the database if needed
  (timbre/info (str "db initialized ? : " (schema/initialized?)))
  (if-not (schema/initialized?) (schema/create-tables)) 

  (timbre/info "myapp started successfully")

  )

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "shutting down..."))

;;append your application routes to the all-routes vector
(def all-routes [home-routes app-routes])

(def app (-> all-routes
             middleware/app-handler
             ;;add your middlewares here
             mymiddleware/base-handler
             ))

(def war-handler (middleware/war-handler app))
