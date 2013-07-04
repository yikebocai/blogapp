(ns myapp.models.quartzite
  (:use [clojurewerkz.quartzite.jobs :only [defjob]]
        [clojurewerkz.quartzite.schedule.simple])
  	(:require [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.triggers :as t]
            [clojurewerkz.quartzite.jobs :as j]
            [taoensso.timbre :as timbre]
            [myapp.models.config :as config] 
            [myapp.models.sync :as sync] ))
 


(defjob myjob [ctx]
	(let [path (config/get-value "path")
		  url (config/get-value "url")]
		(do 
			(timbre/info "Sync github blogs ...")
			(sync/sync-blog path url))))

(defn myschedule
  [& m]
  (qs/initialize)
  (qs/start)
  (let [job (j/build
              (j/of-type myjob)
              (j/with-identity (j/key "jobs.noop.1")))
        tk      (t/key "triggers.1")
        period (java.lang.Integer/parseInt (config/get-value "period"))
        trigger (t/build
                  (t/with-identity tk)
                  (t/start-now)
                  (t/with-schedule (schedule
                                     (repeat-forever)
                                     ;(with-repeat-count 10)
                                     (with-interval-in-hours period))))]
  ;; submit for execution
  (qs/schedule job trigger)
  ;; and immediately unschedule the trigger
  ;(qs/unschedule-job tk)
  ))