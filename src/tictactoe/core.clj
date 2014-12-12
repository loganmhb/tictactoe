(ns tictactoe.core
  (:gen-class)
  (:require [tictactoe.ai :refer [best-move]]
            [tictactoe.game :refer [winner legal-moves new-game make-move]]))


;;;; Command line interface

(defn contents->str
  "Translate game data to strings for printing."
  [contents]
  (case contents
    :e " "
    :x "X"
    :o "O"))

(defn select-side
  "Take player input to select side."
  []
  (println "Select a side. (x/o)")
  (loop [player-side (read-line)]
    (if (not (or (= player-side "x")
                 (= player-side "o")))
      (do (print "That is not an acceptable input. Please type 'x' or 'o': ")
          (recur (read-line)))
      (case player-side
        "x" :x
        "o" :o))))

(defn print-row
  [row]
  (let [r (map contents->str row)]
    (str " " (first r) " | " (second r) " | " (last r) " ")))

(defn print-game
  [game]
  (let [rows (map print-row (:board game))]
    (println (first rows))
    (println "-----------")
    (println (second rows))
    (println "-----------")
    (println (last rows))))

(defn str->move
  "Converts a string of form 'x y' to a vector [x y]."
  [s]
  (vector (Integer. (str (first s))) (Integer. (str (last s)))))

(defn prompt-move
  "Get player move from stdin."
  [game]
  (print-game game)
  (println "Please enter a move in the form 'row column' (row and col being 0-2):")
  (loop [move-string (read-line)]
    (if (re-matches #"[0-2] [0-2]" move-string)
      (let [move (str->move move-string)]
        (if (filter #(= % move) (legal-moves game))
          move
          (do (println "Invalid move. Square is occupied. Please try again.")
              (recur (read-line)))))
      (do (println "Invalid format. Format is 'x y'.")
          (recur (read-line))))))

(defn play
  "Play the game against the computer as the given side (should be keyword :x or :o)."
  [player-side]
  (loop [game (new-game)]
    (cond (winner game) (do (print-game game)
                            (println "Winner is " (contents->str (winner game))))
          (empty? (legal-moves game)) (do (print-game game)
                                          (println "The game is a draw."))
          ;; Get move from human if it is their turn.
          (= (:to-move game) player-side) (recur (make-move game (prompt-move game)))
          ;; Otherwise, let the computer pick the best move.
          :else (recur (make-move game (best-move game))))))

(defn -main
  "CLI interface for playing Tic Tac Toe."
  [& args]
  (println "Welcome to tic tac toe.")
  (play (select-side)))
