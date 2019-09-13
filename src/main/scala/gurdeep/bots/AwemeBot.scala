package gurdeep.bots

import gurdeep.data.Databank
import io.scalac.slack.MessageEventBus
import io.scalac.slack.bots.AbstractBot
import io.scalac.slack.common.{Command, OutboundMessage}

case class Fact(fact: String)

class AwemeBot(override val bus: MessageEventBus) extends AbstractBot {

  override def help(channel: String): OutboundMessage =
    OutboundMessage(channel, s"$name will present you a random tech fact")

  override def act: Receive = {
    case Command("aweme", _, message) =>
      val response = OutboundMessage(message.channel, Databank.randomFact)

      publish(response)
  }
}