(ns web.core)

(defn handler [request]
  {:status 404
   :headers {"Content-type" "text/html"}
   :body (str "Hello World.<br>Your IP is " (:remote-addr request)
              "<br>request-method : " (:request-method request)
              "<br>headers : " (:headers request))
   }
)