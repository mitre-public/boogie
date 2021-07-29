(ns boogie.arinc.cycles-test
  (:require [clojure.test :refer :all]
            [boogie.arinc.cycles :refer [cycle-file find-available-files]]))

(deftest test-cycle-file
  (is (= "/data/cda/raw/cifp/copy/2001/FAACIFP18" (.getAbsolutePath (cycle-file "2001")))))

; when run on the remote bamboo servers these _can_ see the linux FS... so the results are different :facepalm:
;(deftest test-find-available-files
;  (is (= {} (find-available-files))))