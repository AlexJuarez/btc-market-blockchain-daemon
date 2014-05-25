(ns lobos.migrations
  (:refer-clojure
     :exclude [alter drop bigint boolean char double float time])
       (:use (lobos [migration :only [defmigration]] core schema config helpers)))

(defmigration add-transactions-table
  (up [] (create
          (tbl :transaction
               (varchar :id 64 :primary-key))))
  (down [] (drop (table :transaction))))

(defmigration add-blocks-table
  (up [] (create
          (tbl :block
               (varchar :id 64 :primary-key))))
  (down [] (drop (table :block))))
