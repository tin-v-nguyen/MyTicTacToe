package com.mytictactoe.controller;

import com.mytictactoe.controller.dto.ConnectRequest;
import com.mytictactoe.exception.InvalidGameException;
import com.mytictactoe.exception.InvalidMoveException;
import com.mytictactoe.exception.NotFoundException;
import com.mytictactoe.model.Game;
import com.mytictactoe.model.Move;
import com.mytictactoe.model.Player;
import com.mytictactoe.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player) {
        log.info("start game request: {}", player);
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest request) throws Exception {
        log.info("connect request: {}", request);
        return ResponseEntity.ok(gameService.connectToGame(request.getPlayer(), request.getGameId()));
    }

    @PostMapping("/connect/random")
    public ResponseEntity<Game> connectRandom(@RequestBody Player player2) throws NotFoundException {
        log.info("connect random {}", player2);
        return ResponseEntity.ok(gameService.connectToRandomGame(player2));
    }

    @PostMapping("/makeMove")
    public ResponseEntity<Game> makeMove(@RequestBody Move move) throws NotFoundException, InvalidGameException, InvalidMoveException {
        log.info("move: {}", move);
        Game game = gameService.makeMove(move);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

}
