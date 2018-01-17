if (window.console) {
    console.log("Welcome to the Ummikub Play application's JavaScript!");
}

$(document).ready(function () {

    var PrintSetToPlayingFieldEvent = "PrintTileSetsToPlayingFieldEvent";
    var PrintRackEvent = "PrintRackEvent";
    var PrintPossibleSetsEvent = "PrintPossibleSetsEvent";
    var CleanSetsAndAppendsEvent = "CleanPossibleSetsAndAppendsEvent";

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
            case PrintSetToPlayingFieldEvent:
                printTileSetsToPlayingField(jsonData);
                break;
            case PrintRackEvent:
                printRack(jsonData);
                break;
            case PrintPossibleSetsEvent:
                printPossibleSets(jsonData);
                break;
            case CleanSetsAndAppendsEvent:
                cleanSetsAndAppends();
                break;
        }


        //dummyMethodToPrintSomething(jsonData);
    }
});

function cleanSetsAndAppends() {
    var possibleSetsPanelContent = document.getElementById("possibleSetsPanelContent");
    possibleSetsPanelContent.remove();

    // TODO clean appends
}

function printPossibleSets(json) {
    var possibleSetsPanelContent = document.getElementById("possibleSetsPanelContent");
    if(possibleSetsPanelContent != null) possibleSetsPanelContent.remove();

    var possibleSetsPanel = document.getElementById("possibleSetsPanel");
    var possibleSets = json["html"];
    var div = document.createElement("DIV");
    div.id = "possibleSetsPanelContent";
    div.innerHTML = possibleSets;
    possibleSetsPanel.appendChild(div);
}

function printRack(json) {
    var rackPanelContent = document.getElementById("rackPanelContent");
    rackPanelContent.remove();

    var rackPanel = document.getElementById("rackPanel");
    var rack = json["html"];
    var div = document.createElement("DIV");
    div.id = "rackPanelContent";
    div.innerHTML = rack;
    rackPanel.appendChild(div);
}

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
