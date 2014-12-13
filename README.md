# tictactoe

This is an unbeatable Tic Tac Toe game that can be played over a command line interface. Instead of a minimax-style search of the game tree, it uses Newell & Simon's heuristics for perfect play -- more interesting to code and better performance to boot.

## Installation

To run with Leiningen, clone the repository and run:

    cd tictactoe
    lein run

If you don't want to use lein, run the uberjar:

    java -jar target/uberjar/tictactoe-0.1.0-standalone.jar

## Usage

The source is structured so that the game logic is easy to extract from the CLI. game.clj contains the rules of the game, and ai.clj contains the code implementing Newell & Simon's heuristics.

## Options

No command line options yet.

### Bugs

Incomplete test coverage. Is it really unbeatabe?

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
