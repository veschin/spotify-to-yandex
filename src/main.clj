(ns main
;;   (:gen-class)
  (:require clj-http.client
            clojure.tools.reader
            clojure.string))

;; U NEED TO ADD BEARER OAuth Token
;; WITH user-library-read PERMISSION 
;; FROM 
;; https://developer.spotify.com/console/get-current-user-saved-tracks
(def bearer nil)

(defn spotify->edn [& [berear-]]
  (spit "spotify.edn" "[")
  (loop [link "https://api.spotify.com/v1/me/tracks?offset=0&limit=50&market=RU"]
    (let [res (clj-http.client/request
               {:url     link
                :method  :get
                :as      :json
                :headers {"Accept"        "application/json"
                          "Content-Type"  "application/json"
                          "Authorization" (str "Bearer " (or bearer berear-))}})
          next (-> res :body :next)]
      (when next
        (spit "spotify.edn"
              {:link (-> res :body :href) :body (-> res :body)}
              :append true)
        (prn "now" (-> res :body :href) next (:status res))
        (Thread/sleep 1000)
        (recur (-> res :body :next)))))
  (spit "spotify.edn" "]" :append true))

(defn edn->txt []
  (let [->song (fn [{name :name [artist & _] :artists album :album}]
                 (str (:name artist) " - " name " - " (:name album) "\n"))
        spotify-edn (-> "spotify.edn"
                        slurp
                        clojure.tools.reader/read-string)
        tracks (for [{items :items} (map :body spotify-edn)
                     {track :track} items]
                 (->song track))]
    (->> tracks
         (clojure.string/join "")
         (spit "spotify.txt"))))

(defn main [& [{berear- :berear-}]]
  (prn berear-)
  (spotify->edn berear-)
  (edn->txt))
