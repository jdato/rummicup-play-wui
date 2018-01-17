package services

import akka.actor.{Actor, ActorSelection}
import model.Messages.{Init, RegisterObserver}

class WuiViewActor extends Actor{

  val controller: ActorSelection = context.system.actorSelection("user/controller")
  controller ! RegisterObserver

  override def receive: Receive = {
    case Init =>
  }
}
