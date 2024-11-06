package com.mytictactoe.controller.dto;

import com.mytictactoe.model.Player;
import lombok.Data;

@Data
public class ConnectRequest {
    private Player player;
    private String gameId;
}
