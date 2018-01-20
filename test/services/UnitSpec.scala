package services

import model.UiTile
import org.scalatestplus.play.PlaySpec

import scala.collection.mutable.ListBuffer


/**
  * Unit tests can run without a full Play application.
  */
class UnitSpec extends PlaySpec {

  "TileGeneratorService" should {

    val service: TileSetGerneratorService = new TileSetGerneratorService

    "return a valid single digit tile" in {

      val tile: UiTile = service.generateTile("blue", 5)
      val html: String = tile.buildTileHtml()

      html must include("tileNumberSingle")
      html must include("color:blue")
      html must include(">5<")
    }

    "return a valid double digit tile" in {

      val tile: UiTile = service.generateTile("red", 10)
      val html: String = tile.buildTileHtml()

      html must include("tileNumberDouble")
      html must include("color:red")
      html must include(">10<")
    }

    "return a valid arrow tile" in {

      val tile: UiTile = service.generateTile("", 42)
      val html: String = tile.buildTileHtml()

      html must include("src=\"assets/images/arrow.png\"")
    }

    "return a valid tileSet" in {

      val buf = new ListBuffer[UiTile]
      buf += service.generateTile("red", 8)
      buf += service.generateTile("", 42)
      buf += service.generateTile("red", 9)
      buf += service.generateTile("red", 10)
      buf += service.generateTile("red", 11)

      val html: String = service.generateTileSetHtmlString(buf)

      html must include("<div class=\"tileSet\">")

      html must include("tileNumberSingle")
      html must include("color:red")
      html must include(">8<")

      html must include("src=\"assets/images/arrow.png\"")

      html must include("tileNumberSingle")
      html must include("color:red")
      html must include(">9<")

      html must include("tileNumberDouble")
      html must include("color:red")
      html must include(">10<")

      html must include("tileNumberDouble")
      html must include("color:red")
      html must include(">11<")
    }

  }

}
