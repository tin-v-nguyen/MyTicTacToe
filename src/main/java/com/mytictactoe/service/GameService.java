package com.mytictactoe.service;

import com.mytictactoe.model.Game;
import com.mytictactoe.model.GameStatus;
import com.mytictactoe.model.Player;
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
        return game;
    }

}
