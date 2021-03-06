(ns guestbook.db.schema
  (:require [clojure.java.jdbc :as sql]
            [clojure.java.io :refer [file]]
            [noir.io :as io]))

(def db-store (str (.getName (file ".")) "/site.db"))

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2"
              :subname db-store
              :user "sa"
              :password ""
              :make-pool? true
              :naming {:keys clojure.string/lower-case
                       :fields clojure.string/upper-case}})
(defn initialized?
  "checks to see if the database schema is present"
  []
  (.exists (file (str db-store ".mv.db"))))

(defn create-guestbook-table []
  (sql/db-do-commands db-spec
    (sql/create-table-ddl
      :guestbook
      [:id "INTEGER PRIMARY KEY AUTO_INCREMENT"]
      [:timestamp :timestamp]
      [:name "varchar(30)"]
      [:message "varchar(200)"]))
  (sql/db-do-prepared db-spec
    "CREATE INDEX timestamp_index ON guestbook (timestamp)"))

(defn create-tables
  "creates the database tables used by the application"
  []
  (create-guestbook-table))
