const url = 'http://localhost:8080';
let stompClient;
let gameId;
let playerType;

function connectToSocket(gameId) {
    console.log("connecting to the game");
    let socket = new SockJS(url + "/makeMove");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            console.log(data);
            displayResponse(data);
        });
    })
}

function create_game() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Please enter login");
    } else {
        $.ajax({
            url: url + "/game/start",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "login": login
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'X';
                reset();
                connectToSocket(gameId);
                gameOn = true;
                alert("You created a game. Game id is: " + gameId);
            },
            error: function(error) {
                console.log(error);
            }
        })
    }
}

function connectToRandomGame() {
    let login = document.getElementById("login").value;
        if (login == null || login === '') {
            alert("Please enter login");
        } else {
            $.ajax({
                url: url + "/game/connect/random",
                type: 'POST',
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify({
                    "login": login
                }),
                success: function (data) {
                    gameId = data.gameId;
                    playerType = 'O';
                    reset();
                    connectToSocket(gameId);
                    gameOn = false;
                    alert("You joined a random game against: " + data.player1.login);
                },
                error: function(error) {
                    console.log(error);
                }

            });
        }
}

function connectToSpecificGame() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Please enter login");
    } else {
        let gameId = document.getElementById("game_id");
        if (gameId == null || gameId === '') {
            alert("Please enter a game id");
        }
        $.ajax({
            url: url + "/game/connect",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "player" :{
                    "login": login
                },
                "gameId": gameId
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'O';
                reset();
                connectToSocket(gameId);
                gameOn = false;
                alert("You joined a game against: " + data.player1.login);
            },
            error: function(error) {
                console.log(error);
            }

        });
    }
}

