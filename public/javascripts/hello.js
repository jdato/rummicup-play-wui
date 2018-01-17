if (window.console) {
    console.log("Welcome to the Ummikub Play application's JavaScript!");
}

$(document).ready(function () {
    /*
    var gameStartedEvent = "GameStarted";
    var cellChangedEvent = "CellChanged";
    var attackActionResultEvent = "AttackActionResult";
    var playerWonEvent = "PlayerWon";
    */
    if ("WebSocket" in window) {
        console.log("WebSocket is supported by your Browser!");
    } else {
        console.log("WebSocket NOT supported by your Browser!");
        return;
    }
    var getScriptParamUrl = function () {
        var scripts = document.getElementsByTagName('script');
        var lastScript = scripts[scripts.length - 1];
        return lastScript.getAttribute('data-url');
    };

    var url = getScriptParamUrl();
    var connection = new WebSocket(url);

    connection.onmessage = function (event) {

        var jsonData = JSON.parse(event.data);

        dummyMethodToPrintSomething(jsonData);
    }
});

function dummyMethodToPrintSomething(someString) {
    console.log("Application says: " + someString.message)
}
