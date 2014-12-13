(ns tictactoe.game)

;; The game of tic tac toe can be represented as a map with two keys:
;;   :board, mapped to a two-dimensional vector containing :x, :o or :e for each space
;;   :to-move, mapped to the player whose turn it is (:x or :o)

(defn new-game
  "Return a new game with an empty board."
  []
  {:board (vector [:e :e :e]
                  [:e :e :e]
                  [:e :e :e])
   :to-move :x})

;; We need to know which moves are legal.

(defn legal-moves
  "Vector of all possible moves on a given board."
  [game]
  (let [all-moves (for [x [0 1 2] y [0 1 2]] [x y])]
    (filter (fn [[x y]] (= (((:board game) x) y) :e)) all-moves)))

(defn switch-player [to-move]
  (if (= to-move :x) :o :x))

(defn make-move
  "Make a move given in [x y] coords on the given board. If space is already occupied,
   returns the old game."
  [game [x y]]
  (if (filter #(= % [x y]) (legal-moves game)) ; move is legal
    {:board (update-in (:board game) [x y] (fn [_] (:to-move game))),
     :to-move (switch-player (:to-move game))}
    game))

(defn three-in-a-row? [row]
  (if (and (= (first row) (second row))
           (= (first row) (last row)))
    (if (not= (first row) :e)
      (first row)
      nil)
    nil))

;; To find out if a player has won, map three-in-a-row to each row, column and diagonal.
;; (Adapted from my 4clojure solution to the same problem.)

(defn winner
  "Returns :x, :o or nil."
  [game]
  (let [rows (concat (:board game) ; rows
                     (for [x [0 1 2]] (map #(get % x) (:board game))) ; columns
                     ;; Ugly code to get the diagonals
                     (list (mapv #((get (:board game) %) %) [0 1 2])
                           (mapv #((get (vec (reverse (:board game))) %) %) [2 1 0])))]
    ;; Look for a non-nil value (assumes two players can't get three in a row)
    (first (filter identity (map three-in-a-row? rows)))))
