(ns datomic-free-test.c4-db-helpers
  (:require [datomic.api :as d]))

(defn eid [db attr val]
  (->> (d/q '[:find ?e :in $ ?a ?v :where [?e ?a ?v]] db attr val)
       ffirst))

(defn attrs [db attr val]
  (some->> (eid db attr val)
           (d/entity db)
           d/touch))
