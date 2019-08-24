package gurdeep

import akka.actor.{ActorContext, ActorRef, ActorSystem, Props}
import gurdeep.bots.{DefineBot, HitmeBot}
import io.scalac.slack.api.Start
import io.scalac.slack.bots.system.{CommandsRecognizerBot, HelpBot}
import io.scalac.slack.common.Shutdownable
import io.scalac.slack.common.actors.SlackBotActor
import io.scalac.slack.websockets.WebSocket
import io.scalac.slack.{BotModules, MessageEventBus}

object GurdeepRunner extends Shutdownable {
  val system = ActorSystem("GurdeepSystem")
  val eventBus = new MessageEventBus
  val slackBot = system.actorOf(Props(classOf[SlackBotActor], new GurdeepModules(), eventBus, this, None), "gurdeep")

  def main(args: Array[String]) {
    println("Gurdeep started")

    try {
      slackBot ! Start

      system.awaitTermination()
      println("Shutdown successful...")
    } catch {
      case e: Exception =>
        println("An unhandled exception occurred...", e)
        system.shutdown()
        system.awaitTermination()
    }
  }

  sys.addShutdownHook(shutdown())

  override def shutdown(): Unit = {
    slackBot ! WebSocket.Release
    system.shutdown()
    system.awaitTermination()
  }

  class GurdeepModules() extends BotModules {
    override def registerModules(context: ActorContext, websocketClient: ActorRef) = {
      context.actorOf(Props(classOf[CommandsRecognizerBot], eventBus), "commandProcessor")
      context.actorOf(Props(classOf[HelpBot], eventBus), "helpBot")
      context.actorOf(Props(classOf[DefineBot], eventBus), "defineBot")
      context.actorOf(Props(classOf[HitmeBot], eventBus), "hitmeBot")
    }
  }
}