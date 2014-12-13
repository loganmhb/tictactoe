(ns tictactoe.core-test
  (:require [clojure.test :refer :all]
            [tictactoe.core :refer :all]
            [tictactoe.game :refer [make-move new-game legal-moves]]))

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
