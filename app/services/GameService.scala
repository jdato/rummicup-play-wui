package services

import javax.inject.Singleton

import akka.actor.{ActorSystem, Props}
import controller.Controller
import model.Messages.{Init, Input}
import view.gui.SwingGuiViewActor
import view.tui.TuiView

@Singleton
class GameService {

  val actorSystem = ActorSystem.create("rummikubAS")

  actorSystem.actorOf(Props[Controller], "controller")
  val tui = actorSystem.actorOf(Props[TuiView], "tuiActor")
  val gui = actorSystem.actorOf(Props[SwingGuiViewActor], "guiActor")
  val wui = actorSystem.actorOf(Props[WuiViewActor], "wuiActor")

  tui ! Init
  gui ! Init
  wui ! Init

}
