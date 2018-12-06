(ns datomic-free-test.c4-db-api
  (:require [datomic.api :as d]
            [datomic-free-test.c4-db-helpers :as d-help]))

(def db-url "datomic:free://localhost:4334/db")

(def conn (d/connect db-url))


;; Users


(defn get-all-users
  "Get all users
  GET /users"
  []
  (d/q '[:find (pull ?e [*])
         :where [?e :users/login _]] (d/db conn)))

(defn get-user
  "Get user by login
  GET /users/:login"
  [login]
  (->
   (d/q '[:find (pull ?e [*])
          :in $ ?login
          :where [?e :users/login ?login]]
        (d/db conn) login)
   ffirst))

(defn add-user
  "Add new user
  POST /users"
  [login full-name]
  (d/transact conn
              [{:db/id (d/tempid :db.part/user)
                :users/login login
                :users/full-name full-name}]))

(defn delete-user
  "Delete user by login
  DELETE /users/:login"
  [login]
  (d/transact
   conn
   [[:db/retractEntity
     (d-help/eid (d/db conn) :users/login login)]]))

(defn update-user
  "Update user by login
  UPDATE /users/:login"
  [login full-name]
  (let [db (d/db conn)]
    (d/transact conn
                [{:db/id                 (d-help/eid db :users/login login)
                  :users/full-name       full-name}])))

;; Issues


(defn get-all-issues
  "Get all issues
  GET /issues"
  []
  (d/q '[:find (pull ?e [*])
         :where [?e :issues/id _]] (d/db conn)))

(defn get-issue
  "Get issue by id
  GET /issues/:id"
  [id]
  (->
   (d/q '[:find (pull ?e [*])
          :in $ ?id
          :where [?e :issues/id ?id]]
        (d/db conn) id)
   ffirst))

(defn add-issue
  "Add new issue
  POST /issues"
  [title description author-login assignee-login]
  (let [db (d/db conn)
        inc-number (d/q '[:find (max ?number) . :in $ :where [_ :issues/id ?number]] db)]
    (d/transact conn
                [{:db/id                 (d/tempid :db.part/user)
                  :issues/id             (inc (or inc-number 0))
                  :issues/title          title
                  :issues/description    description
                  :issues/author         (d-help/eid db :users/login author-login)
                  :issues/assignee       (d-help/eid db :users/login assignee-login)
        ;;        :issues/execution-date execution-date
                  }])))

(defn delete-issue
  "Delete issue by id
  DELETE /issue/:id"
  [id]
  (d/transact
   conn
   [[:db/retractEntity
     (d-help/eid (d/db conn) :issues/id id)]]))

(defn update-issue
  "Update issue by id
  UPDATE /issue/:id"
  [id title description author-login assignee-login]
  (let [db (d/db conn)]
    (d/transact conn
                [{:db/id                 (d-help/eid db :issues/id id)
                  :issues/title          title
                  :issues/description    description
                  :issues/author         (d-help/eid db :users/login author-login)
                  :issues/assignee       (d-help/eid db :users/login assignee-login)
        ;;        :issues/execution-date execution-date
                  }])))


;; Add mock data


(defn create-mock-data
  []
  (add-user "test" "tests1")
  (add-user "test2" "tests2")
  (add-issue "issue 99" "desc 1" "lve" "test")
  (add-issue "issue 2" "desc 2" "lve" "test")
  (add-issue "issue 3" "desc 3" "test" "lve"))




