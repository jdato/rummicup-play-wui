if (window.console) {
    console.log("Welcome to the Ummikub Play application's JavaScript!");
}

$(document).ready(function () {

    var printSetToPlayingFieldEvent = "PrintTileSetsToPlayingField";

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
        switch (jsonData.event) {
            case printSetToPlayingFieldEvent:
                printTileSetsToPlayingField(jsonData);
                break;
        }


        //dummyMethodToPrintSomething(jsonData);
    }
});

function printTileSetsToPlayingField(json) {
    var playingFieldPanelContent = document.getElementById("playingFieldPanelContent");
    playingFieldPanelContent.remove();

    var playingFieldPanel = document.getElementById("playingFieldPanel");
    var tileSets = json["html"];
    var div = document.createElement("DIV");
    div.id = "playingFieldPanelContent";
    div.innerHTML = tileSets;
    playingFieldPanel.appendChild(div);
}

function dummyMethodToPrintSomething(someString) {
    console.log("Application says: " + someString.message)
}
