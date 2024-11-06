package com.mytictactoe.service;

import com.mytictactoe.exception.InvalidGameException;
import com.mytictactoe.exception.InvalidMoveException;
import com.mytictactoe.exception.InvalidParamException;
import com.mytictactoe.exception.NotFoundException;
import com.mytictactoe.model.*;
import com.mytictactoe.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GameService {

    public Game createGame(Player player) {
        Game game = new Game();
        game.setBoard(new int[3][3]);
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(player);
        game.setStatus(GameStatus.NEW);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game connectToGame(Player player2, String gameId) throws Exception {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) throw new InvalidParamException("Game with provided id doesnt exist");

        Game game = GameStorage.getInstance().getGames().get(gameId);

        if (game.getPlayer2() != null) throw new InvalidGameException("Game with provided id is full");
        if (game.getStatus() == GameStatus.FINISHED) throw new InvalidGameException("Game is already finished");

        game.setPlayer2(player2);
        game.setStatus(GameStatus.IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game connectToRandomGame(Player player2) throws NotFoundException {
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Game not found"));
        game.setPlayer2(player2);
        game.setStatus(GameStatus.IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game makeMove(Move move) throws NotFoundException, InvalidGameException, InvalidMoveException{
        if (!GameStorage.getInstance().getGames().containsKey(move.getGameId())) {
            throw new NotFoundException("Game not found");
        }
        Game game = GameStorage.getInstance().getGames().get(move.getGameId());
        if (game.getStatus().equals(GameStatus.FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }
        if (game.getStatus().equals(GameStatus.NEW)) {
            throw new InvalidGameException("Game doesn't have enough players");
        }
        int [][] board = game.getBoard();
        if (board[move.getX()][move.getY()] != 0) throw new InvalidMoveException("Slot is occupied");
        board[move.getX()][move.getY()] = move.getTurn().getValue();

        if (checkWinner(game.getBoard(), TicToe.X)) {
            game.setWinner(TicToe.X);
            game.setStatus(GameStatus.FINISHED);
        } else if (checkWinner(game.getBoard(), TicToe.O)) {
            game.setWinner(TicToe.O);
            game.setStatus(GameStatus.FINISHED);
        }

        GameStorage.getInstance().setGame(game);
        return game;
    }

    private Boolean checkWinner(int[][] board, TicToe ticToe) {
        int[] boardArray = new int[9];
        int index = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardArray[index++] = board[i][j];
            }
        }
        int[][] winCombos = {
                // horizontal
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                // diag
                {0, 4, 8}, {2, 4, 6},
                // vertical
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}
        };
        for (int i = 0; i < winCombos.length; i++) {
            int counter = 0;
            for (int j = 0; j < winCombos[i].length; j++) {
                if (boardArray[winCombos[i][j]] != ticToe.getValue()) break;
                counter++;
                if (counter == 3) return true;
            }
        }
        return false;

    }

}
