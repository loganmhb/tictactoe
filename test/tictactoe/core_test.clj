(ns tictactoe.core-test
  (:require [clojure.test :refer :all]
            [tictactoe.core :refer :all]))

(deftest move-making
  (testing "Move-making."
    (is (make-move (new-game) [1 1])
        {:board [[:e :e :e] [:e :x :e] [:e :e :e]],
         :to-move :o})
    (is (make-move {:board [[:x :o :x] [:o :x :x] [:o :x :o]],
                    :to-move :o}
                   [2 2])
        nil)))

(deftest move-generation
  (testing "legal-moves generates only legal moves."
    (is (count (legal-moves (new-game))) 9)))

(deftest ai
  (testing "Newell & Simon's rules."
    (is (fork? {:board [[:x :e :x] [:o :x :e] [:o :o :e]] :to-move :o}))
    (is (= [0 1]
           (find-fork? {:to-move :x, :board [[:x :e :e] [:o :x :e] [:e :e :o]]})))))
