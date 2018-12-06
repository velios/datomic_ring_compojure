(ns ring-test.compojure
  (:require [ring.util.response :refer [response content-type redirect]]
            [compojure.core :refer [GET POST defroutes context]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.session :refer [wrap-session]]))


(defn main-page [my-count]
  (-> (response (str "You've accessed " (or my-count 0) " times"))
      (assoc :session {:my-count (inc (or my-count 0))})
      (content-type "text/plain")))


(defroutes my-handler
  (GET "/req" [] (str "Request from my-handler")))


(defn upload [submit-name {:keys [tempfile filename]}]
  (let [out (io/file filename)]
    (io/copy tempfile out)
    (response (str submit-name ", file is submitted!"))))

(defroutes compojure-handler
  (context "/my" [] my-handler)

;;   File upload
  (GET "/form" [] "<form action='/form'
       enctype='multipart/form-data'
       method='post'>
       Name: <input type='text' name='submit-name'/></br>
       File: <input type='file' name='myfile'/></br>
       <input type='submit' value='Send'></form>")
  (POST "/form" [submit-name myfile]
        (upload submit-name myfile))


  (GET "/a" [:as req] (main-page (:my-count (:session req))))
  (GET "/" {{my-count :my-count} :session} (main-page my-count))
;;   (GET "/" request (str request))
  (GET "/req" request (str request))
  (route/resources "/")
  (route/not-found (slurp (io/resource "public/denied.html"))))

(def app
  (wrap-defaults compojure-handler (assoc-in site-defaults [:security :anti-forgery] false) ))
