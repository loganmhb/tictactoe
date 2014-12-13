(ns tictactoe.ai
  (:require [tictactoe.game :refer [winner make-move legal-moves switch-player]]))

;;; Functions for implementing Newell & Simon's rules for perfect play

;; Note: these functions do not check
;; for redundancies; i.e. block-win and block-fork assume the opponent will have
;; at most one way to win or fork. This simplifies the code and does not detract
;; from perfect play as long as the rules are applied in the correct order.

(defn winning-move
  "Returns winning move or nil if no move immediately wins."
  [game]
  (first (filter (fn [move]
                   (winner (make-move game move)))
                 (legal-moves game))))

(defn null-move
  "Returns the same game with the other player to move, which is useful
   for evaluating threats."
  [game]
  {:board (:board game)
   :to-move (switch-player (:to-move game))})

(defn block-win
  "Returns the move necessary to prevent an opponent win,
   or nil if no immediate block is required."
  [game]
  ;; Uses a null-move idea
  (winning-move (null-move game)))

(defn fork?
  "Returns true if a position is a fork (the NON-moving player has two
   ways to win)."
  [game]
  (< 1 (count (filter winner
                      (map (partial make-move (null-move game))
                           (legal-moves (null-move game)))))))

(defn find-fork
  "Returns a move creating a fork (two chances to win, i.e. guaranteed win
   next turn if opponent can't get three in a row) or nil."
  [game]
  (first (filter #(fork? (make-move game %)) (legal-moves game))))

(defn threats
  "Finds all threats that will force the opponent to defend."
  [game]
  (filter (fn [move] (-> (make-move game move)
                         null-move
                         winning-move))
          (legal-moves game)))

(defn block-fork
  "Returns a move forcing the opponent to defend if possible, unless that will
   result in a fork, in which case returns a move blocking the fork."
  [game]
  (if-let [good-threats
           (filter (fn [threat]
                     (let [threat-result (make-move game threat)]
                       (not (fork? (make-move threat-result
                                              (block-win threat-result))))))
                   (threats game))]
    (first good-threats)
    ;; No good threat exists
    (if-let [move (find-fork (null-move game))]
      move
      ;; No threat of fork either
      nil)))

(defn center
  "Returns the center if it is a valid move."
  [game]
  (first (filter #(= % [1 1]) (legal-moves game))))

(defn corner? [[x y]]
  (and (or (= x 0) (= x 2))
       (or (= y 0) (= y 2))))

(defn opposite-corner
  "Return the opposite corner."
  [corner]
  (mapv #(if (= % 0) 2 0) corner))

(defn move-opposite-corner
  "If the opponent has moved in a corner, move in the opposite corner."
  [game]
  (first (filter (fn [[x y]]
                   (and (corner? [x y])
                        ;; Opponent has moved in opposite corner
                        (= (get-in game (into [:board] (opposite-corner [x y])))
                           (switch-player (:to-move game)))))
                 (legal-moves game))))

(defn move-corner-or-side
  "Moves in an empty corner, if available, or a side if not."
  [game]
  (if-let [corners (filter corner? (legal-moves game))]
    (first corners)
    (first (legal-moves game))))

(defn best-move
  "Return the best move the computer can find, using Newell & Simon's rules
   to guarantee perfect play."
  [game]
  (some #(% game) [winning-move
                   block-win
                   find-fork
                   block-fork
                   center
                   move-opposite-corner
                   move-corner-or-side]))
