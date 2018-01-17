package services

import akka.actor.{Actor, ActorSelection}
import controllers.HomeController
import model.Messages._
import model._

import scala.collection.mutable.ListBuffer

class WuiViewActor(homeController: HomeController) extends Actor {

  val tileSetGerneratorService: TileSetGerneratorService = new TileSetGerneratorService
  val controller: ActorSelection = context.system.actorSelection("user/controller")
  controller ! RegisterObserver

  override def receive: Receive = {
    case Init => homeController.sendGameStartedJsonToClient("Init Game")
    case StartGame => homeController.sendGameStartedJsonToClient("Started Game")
    case AbortGame => homeController.sendGameStartedJsonToClient("Aborted Game")
    case GameOver(player: Player) => homeController.sendGameStartedJsonToClient("Game Over, player " + player.id + " won!")
    case PrintMessage(s: String) => homeController.sendGameStartedJsonToClient(s)

    case PrintPlayingField(player: Player, playingfield: Playingfield) => {
      printPlayingField(playingfield)
      printRack(player)
    }
    case PrintPossibleTileSets(possibleSets: List[TileSet]) => printPossibleSets(possibleSets)
    case PrintPossibleAppendsToTileSets(tilesToAppendToTileSet: Map[Tile, TileSet]) => printPossibleAppends(tilesToAppendToTileSet)

    case SetTiles(input: String) => removePossibleMoves()

    case _ => // homeController.sendGameStartedJsonToClient("No important action for wui")
  }

  def printPlayingField(playingfield: Playingfield): Unit = {
    removePossibleMoves()
    var result = ""
    playingfield.playedTileSets.foreach(tileSet => {
      val uiTilesList: ListBuffer[UiTile] = ListBuffer()
      tileSet.tiles.foreach(tile => {
        uiTilesList += tileSetGerneratorService.generateTile(tile.color, tile.number)
      })
      result += tileSetGerneratorService.generateTileSetHtmlString(uiTilesList)
    })
    homeController.printPlayingField(result: String)
  }

  def printRack(player: Player): Unit = {
    val uiTilesList: ListBuffer[UiTile] = ListBuffer()
    player.rack.tiles.foreach(tile => {
      uiTilesList += tileSetGerneratorService.generateTile(tile.color, tile.number)
    })
    homeController.printRack(tileSetGerneratorService.generateTileSetHtmlString(uiTilesList))
  }

  def printPossibleSets(possibleSets: List[TileSet]): Unit = {
    var result = ""
    possibleSets.foreach(tileSet => {
      val uiTilesList: ListBuffer[UiTile] = ListBuffer()
      tileSet.tiles.foreach(tile => {
        uiTilesList += tileSetGerneratorService.generateTile(tile.color, tile.number)
      })
      result += tileSetGerneratorService.generateTileSetHtmlString(uiTilesList)
    })
    homeController.printPossibleSets(result)
  }

  def printPossibleAppends(tilesToAppendToTileSet: Map[Tile, TileSet]): Unit = {
    var result = ""
    tilesToAppendToTileSet.foreach(possibeAppend => {
      val appendTilesList: ListBuffer[UiTile] = ListBuffer()

      appendTilesList += tileSetGerneratorService.generateTile(possibeAppend._1.color, possibeAppend._1.number)

      appendTilesList += tileSetGerneratorService.generateTile("", 42)

      possibeAppend._2.tiles.foreach(tile => {
        appendTilesList += tileSetGerneratorService.generateTile(tile.color, tile.number)
      })
      result += tileSetGerneratorService.generateTileSetHtmlString(appendTilesList)
    })
    homeController.printPossibleAppends(result)
  }

  def removePossibleMoves(): Unit = {
    homeController.removePossibleMoves()
  }

}
