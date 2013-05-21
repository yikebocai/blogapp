(ns myapp.models.gitpull
	(:use [clojure.java.shell :only [sh]]))

(defn sync-blog[path url]
	(sh "pwd"))