package controllers

import javax.inject._

import akka.actor.{Actor, ActorSelection, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage, WebSocketRequest}
import akka.stream.Materializer
import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, MergeHub, Sink, Source}
import akka.{Done, NotUsed}
import model.Messages._
import play.api.libs.json.{JsObject, Json}
import play.api.mvc._
import services.GameService

import scala.Option.empty
import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject()(cc: ControllerComponents)
                              (implicit actorSystem: ActorSystem,
                               mat: Materializer,
                               executionContext: ExecutionContext,
                               webJarsUtil: org.webjars.play.WebJarsUtil,
                               assets: Assets)
  extends AbstractController(cc) {

  private var webSocketUrlOpt: Option[String] = empty
  private type WSMessage = String

  def sendGameStartedJsonToClient(message: String): Unit = {

    val jsonObject = Json.obj(
      "message" -> message
    )

    sendJsonToClient(jsonObject)
  }

  // WS Stuff

  def sendJsonToClient(json: JsObject): Unit = {
    if (webSocketUrlOpt.isDefined) {


      val printSink: Sink[Message, Future[Done]] =
        Sink.foreach {
            case message: TextMessage.Strict =>
            //println(message.text)
        }


      val jsonSource: Source[Message, NotUsed] =
        Source.single(TextMessage(json.toString()))

      val flow: Flow[Message, Message, Future[Done]] =
        Flow.fromSinkAndSourceMat(printSink, jsonSource)(Keep.left)
      Http().singleWebSocketRequest(WebSocketRequest(webSocketUrlOpt.get), flow)
    }
  }

  private val (socketSink, socketSource) = {
    val source = MergeHub.source[WSMessage]
      .log("source")
      .recoverWithRetries(-1, { case _: Exception ⇒ Source.empty })

    val sink = BroadcastHub.sink[WSMessage]
    source.toMat(sink)(Keep.both).run()
  }

  private val userFlow: Flow[WSMessage, WSMessage, _] = {
    Flow.fromSinkAndSource(socketSink, socketSource)
  }

  def index: Action[AnyContent] = Action { implicit request: RequestHeader =>
    new GameService(this)
    webSocketUrlOpt = Option(routes.HomeController.socket().webSocketURL())
    Ok(views.html.index(webSocketUrlOpt.get))
  }

  def socket(): WebSocket = {
    WebSocket.acceptOrResult[WSMessage, WSMessage] {
      _ =>
        Future.successful(userFlow).map { flow =>
          Right(flow)
        }.recover {
          case _: Exception =>
            val msg = "Cannot create websocket"
            val result = InternalServerError(msg)
            Left(result)
        }
    }
  }

}
