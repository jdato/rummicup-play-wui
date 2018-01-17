package services

import javax.inject.Singleton

import akka.actor.{ActorSystem, Props}
import controller.Controller
import controllers.HomeController
import model.Messages.Init
import view.gui.SwingGuiViewActor
import view.tui.TuiView

@Singleton
class GameService(homeController: HomeController) {

  val actorSystem = ActorSystem.create("rummikubAS")

  val controller = actorSystem.actorOf(Props[Controller], "controller")
  val tui = actorSystem.actorOf(Props[TuiView], "tuiActor")
  val gui = actorSystem.actorOf(Props[SwingGuiViewActor], "guiActor")
  val wui = actorSystem.actorOf(Props(new WuiViewActor(homeController)), "wuiActor")

  tui ! Init
  gui ! Init
  wui ! Init

}
