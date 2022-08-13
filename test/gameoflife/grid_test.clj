(ns gameoflife.grid-test
  (:require [clojure.test :refer :all]
            [gameoflife.grid :refer :all]))

(deftest coord->idx-test
  (is (= 0 (coord->idx [0 0] 5)))
  (is (= 5 (coord->idx [0 1] 5)))
  (is (= 10 (coord->idx [0 2] 5)))
  (is (= 1 (coord->idx [1 0] 5)))
  (is (= 2 (coord->idx [2 0] 5)))
  (is (= 24 (coord->idx [4 4] 5))))

(deftest idx->coord-test
  (is (= [0 0] (idx->coord 0 5)))
  (is (= [0 1] (idx->coord 5 5)))
  (is (= [0 2] (idx->coord 10 5)))
  (is (= [1 0] (idx->coord 1 5)))
  (is (= [2 0] (idx->coord 2 5)))
  (is (= [4 4] (idx->coord 24 5))))
