(ns tictactoe.ai-test
  (:require [clojure.test :refer :all]
            [tictactoe.ai :refer :all]))

(deftest best-move-test
  (testing "AI decisionmaking."
    (is (best-move {:board [[:x :e :e] [:e :o :e] [:e :e :x]] :to-move :o}) [0 1])))
