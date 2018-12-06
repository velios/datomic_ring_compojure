(ns datomic-free-test.c4-db-schema
  (:require [datomic.api :as d]))

(def schema
  [
   ;;  issues table

   {:db/ident               :issues/id
    :db/unique              :db.unique/identity
    :db/valueType           :db.type/long
    :db/cardinality         :db.cardinality/one
    :db/id                  (d/tempid  :db.part/db)
    :db.install/_attribute  :db.part/db}

   {:db/ident               :issues/title
    :db/valueType           :db.type/string
    :db/cardinality         :db.cardinality/one
    :db/id                  (d/tempid  :db.part/db)
    :db.install/_attribute  :db.part/db}

   {:db/ident               :issues/description
    :db/valueType           :db.type/string
    :db/cardinality         :db.cardinality/one
    :db/id                  (d/tempid  :db.part/db)
    :db.install/_attribute  :db.part/db}

   {:db/ident               :issues/author
    :db/valueType           :db.type/ref
    :db/cardinality         :db.cardinality/one
    :db/id                  (d/tempid  :db.part/db)
    :db.install/_attribute  :db.part/db}

   {:db/ident               :issues/assignee
    :db/valueType           :db.type/ref
    :db/cardinality         :db.cardinality/one
    :db/id                  (d/tempid  :db.part/db)
    :db.install/_attribute  :db.part/db}

   {:db/ident               :issues/execution-date
    :db/valueType           :db.type/instant
    :db/cardinality         :db.cardinality/one
    :db/id                  (d/tempid  :db.part/db)
    :db.install/_attribute  :db.part/db}

;;  person table

   {:db/ident               :users/login
    :db/unique              :db.unique/identity
    :db/valueType           :db.type/string
    :db/cardinality         :db.cardinality/one
    :db/id                  (d/tempid  :db.part/db)
    :db.install/_attribute  :db.part/db}

   {:db/ident              :users/full-name
    :db/valueType           :db.type/string
    :db/cardinality         :db.cardinality/one
    :db/id                  (d/tempid  :db.part/db)
    :db.install/_attribute  :db.part/db}])
