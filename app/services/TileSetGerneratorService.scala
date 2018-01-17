package services

import model.UiTile

import scala.collection.mutable.ListBuffer

class TileSetGerneratorService {

  def generateTile(color : String, number : Int): UiTile = {
    new UiTile(color, number)
  }

  def generateTileSetHtmlString(tiles : ListBuffer[UiTile]) : String = {
    val tileSetHtmlPrefix = "<div class=\"tileSet\">"
    val tileSetHtmlSuffix = "</div>"

    var result = tileSetHtmlPrefix

    tiles.foreach(tile =>{
      result += tile.buildTileHtml()
    })

    result += tileSetHtmlSuffix
    result
  }

}
