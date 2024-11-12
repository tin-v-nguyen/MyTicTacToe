var turns = [["#","#","#"], ["#","#","#"] , ["#","#", "#"]];
var gameOn = false;

function playerTurn (turn, id) {
    if (gameOn) {
        var spotTaken = $("#"+id).text();
          if (spotTaken === "#") {
            makeAMove(playerType, id.split("_")[0], id.split("_")[1]);
          }
    } else {
    console.log("Not your turn")
    }

}

function makeAMove(type, x, y) {
    $.ajax({
        url: url + "/game/makeMove",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "turn": type,
            "x": x,
            "y": y,
            "gameId": gameId
        }),
        success: function (data) {
            gameOn = false;
            displayResponse(data);

        },
        error: function(error) {
            console.log(error);
        }

    })
}

function displayResponse(data) {
    let board = data.board;
    for (let i = 0; i < board.length; i++) {
        for (let j = 0; j < board[i].length; j++) {
            if (board[i][j] === 1) {
                turns[i][j] = 'X';
            } else if (board[i][j] === 2) {
                turns[i][j] = 'O';
            }
            let cellId = i + "_" + j;
            $("#" + cellId).text(turns[i][j]);
        }
    }
    if (data.winner != null) {
        alert("Winner is " + data.winner);
    }
    gameOn = true;
}

$(".tic").click(function(){
  var slot = $(this).attr('id');
  playerTurn(playerType, slot);
});

function reset() {
  turns = [["#","#","#"], ["#","#","#"] , ["#","#", "#"]];
  $(".tic").text("#");
}

$("#reset").click(function(){
  reset();
});