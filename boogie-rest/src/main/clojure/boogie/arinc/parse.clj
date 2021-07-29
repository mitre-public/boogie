(ns boogie.arinc.parse
  (:require [clojure.string :as str]
            [taoensso.timbre :as timbre :refer-macros [debug info warn]])
  (:import (org.mitre.tdp.boogie.arinc ArincVersion ArincRecordParser)
           (org.mitre.tdp.boogie.arinc.model ConvertingArincRecordMapper ArincRecordConverterFactory)))

(defonce ^ArincRecordParser parser (atom (.parser (ArincVersion/V19))))

;; the converter to use to translate the input ARINC 424 record string into a POJO
(defonce ^ConvertingArincRecordMapper converter (atom (ArincRecordConverterFactory/mapperForVersion (ArincVersion/V19))))

(defn parse-record
  "Returns a map containing the raw and parsed (potentially null/nil) record value."
  [^String record]
  (let [converted (->> [record]
                       (map #(.apply @parser %))
                       (filter #(.isPresent %))
                       (map #(.get %))
                       (map #(.apply @converter %))
                       (filter #(.isPresent %))
                       (map #(.get %))
                       (first))]
    {:raw    record
     :parsed converted}))

(defn arinc->pojo
  "Returns a mapping of {POJO-TypeClass, List[{:raw record :parsed POJO}]} - given the comma-delimited input string of ARINC records.
  Note: if a particular input record couldn't be parsed into a POJO it will be return under the 'unsupported' key and its :parsed value will be null."
  [^String records]
  (timbre/info (str "Attempting to parse input CSV of ARINC records: " records))
  (->> (str/split records #",")
       (map parse-record)
       (group-by (fn [m] (if ((comp nil? :parsed) m) "unsupported" (.getTypeName (.getClass (:parsed m))))))))