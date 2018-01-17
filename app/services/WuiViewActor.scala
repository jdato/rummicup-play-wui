package services

import akka.actor.{Actor, ActorSelection}
import controllers.HomeController
import model.Messages._
import model.{Player, Playingfield, Tile, TileSet}

class WuiViewActor(homeController: HomeController) extends Actor {

  val controller: ActorSelection = context.system.actorSelection("user/controller")
  controller ! RegisterObserver

  override def receive: Receive = {
    case Init => homeController.sendGameStartedJsonToClient("Init Game")
    case StartGame => homeController.sendGameStartedJsonToClient("Started Game")
    case AbortGame => homeController.sendGameStartedJsonToClient("Aborted Game")

    case GameOver(player: Player) => homeController.sendGameStartedJsonToClient("Game Over, player " + player.id + " won!")
    case PrintMessage(s : String) => homeController.sendGameStartedJsonToClient(s)
    case PrintPlayingField(player: Player, playingfield: Playingfield) => homeController.sendGameStartedJsonToClient("Print playing field")
    case PrintPossibleTileSets(tileSets : List[TileSet]) => homeController.sendGameStartedJsonToClient("Print Possible TileSets")
    case PrintPossibleAppendsToTileSets(tilesToAppendToTileSet : Map[Tile, TileSet]) => homeController.sendGameStartedJsonToClient("Print Possible Appends To TileSets")

    case _ => // homeController.sendGameStartedJsonToClient("No important action for wui")
  }

}
