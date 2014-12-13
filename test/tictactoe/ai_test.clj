(ns tictactoe.ai-test
  (:require [clojure.test :refer :all]
            [tictactoe.ai :refer :all]
            [tictactoe.game :refer [new-game]]))

(deftest best-move-test
  (testing "AI decisionmaking."
    (is (best-move {:board [[:x :e :e]
                            [:e :o :e]
                            [:e :e :x]] 
                    :to-move :o}) [0 1])
    (is (best-move (new-game)) [1 1])))

(deftest helper-test
  (testing "Helper functions for the move-attempting functions."
    (is (fork? {:board [[:x :e :x]
                        [:o :x :e]
                        [:e :e :o]]
                :to-move :o}) true)
    (is (threats {:board [[:e :e :x]
                          [:e :o :e]
                          [:e :e :e]]
                  :to-move :x}) '([0 1] [1 2]))))

(deftest heuristic-tests
  (testing "Individual move-attemption functions."
    (is (winning-move {:board [[:x :x :e]
                               [:e :o :o]
                               [:e :e :e]]
                       :to-move :x}) [0 2])
    (is (block-win {:board [[:x :x :e]
                            [:e :o :e]
                            [:e :e :e]]
                    :to-move :o}) [0 2])
    (is (= [0 1]
           (find-fork {:to-move :x, :board [[:x :e :e]
                                             [:o :x :e]
                                             [:e :e :o]]})))))
