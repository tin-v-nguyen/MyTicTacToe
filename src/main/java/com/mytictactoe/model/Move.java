package com.mytictactoe.model;

import lombok.Data;

@Data
public class Move {
    private TicToe turn;
    private Integer x;
    private Integer y;
    private String gameId;
}
