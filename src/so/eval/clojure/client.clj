(ns so.eval.clojure.client
  (:refer-clojure :exclude [read])
  (:require [clj-http.client :as http]
            [cheshire.core :as json]))

(def ^:dynamic *base* "http://eval.so/api")

(defn ^:private request [endpoint type params]
  (let [params (case type
                 :get {:query-params params}
                 :post {:body (json/encode params)
                        :content-type :json})
        result (http/request (merge {:method type
                                     :url (str *base* endpoint)
                                     :throw-exceptions false}
                                    params))]
    (if (= 200 (:status result))
      (json/decode (:body result) true)
      result)))

(defn read
  "Do a GET request to eval.so."
  ([endpoint] (request endpoint :get {}))
  ([endpoint params] (request endpoint :get params)))

(defn write
  "Do a POST request to eval.so."
  ([endpoint] (request endpoint :post {}))
  ([endpoint params] (request endpoint :post params)))
