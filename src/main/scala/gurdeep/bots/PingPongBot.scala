package gurdeep.bots

import java.util.concurrent.TimeUnit

import gurdeep.GurdeepRunner
import io.scalac.slack.bots.IncomingMessageListener
import io.scalac.slack.{MessageEventBus}
import io.scalac.slack.common.{Ping, Pong}

import scala.concurrent.duration.Duration

class PingPongBot extends IncomingMessageListener {

  import context.dispatcher

  override def receive: Receive = {
    case Pong =>
      sendPing()
    case Ping =>
      publish(Ping)
  }

  def sendPing() = {
    context.system.scheduler.scheduleOnce(Duration.create(30, TimeUnit.SECONDS), self, Ping)
  }

  @throws[Exception](classOf[Exception]) override
  def preStart(): Unit = {
    sendPing()
    super.preStart()
  }

  override def bus: MessageEventBus = GurdeepRunner.eventBus
}
