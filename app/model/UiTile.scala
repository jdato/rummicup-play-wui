package model

class UiTile(color: String, number: Int) {
  def buildTileHtml(): String = {
    val prefixTilSingleDoubleDiff = "<span class=\"tile\"><img src=\"assets/images/tile.png\" height=\"100\" alt=\"\"><h2 class=\""
    // single/double css - examle: tileNumberSingle
    val prefixColor = "\"style=\"color:"
    // color - example: red
    val prefixNumber = ";\">"
    // number - example: 1
    val suffix = "</h2></span>"

    if (number == 42) "<span class=\"tile\"><img src=\"assets/images/arrow.png\" height=\"100\" alt=\"\"></span>"
    else {
      if (number > 9) {
        prefixTilSingleDoubleDiff + "tileNumberDouble" + prefixColor + color + prefixNumber + number + suffix
      }
      else {
        prefixTilSingleDoubleDiff + "tileNumberSingle" + prefixColor + color + prefixNumber + number + suffix
      }
    }
  }
}
