package gurdeep.bots

import io.scalac.slack.MessageEventBus
import io.scalac.slack.bots.AbstractBot
import io.scalac.slack.common.{Command, OutboundMessage}

class HitmeBot(override val bus: MessageEventBus) extends AbstractBot {

  override def help(channel: String): OutboundMessage =
    OutboundMessage(channel,
      s"$name will help you to solve difficult math problems \\n" +
      "Usage: $hitme {operation} {arguments separated by space}")

  override def act: Receive = {
    case Command("hitme", _, message) =>
      val response = Dictionary.getOne match {
        case Some(tip) => OutboundMessage(message.channel, s"${tip._1 capitalize} is ${tip._2}")
        case None => OutboundMessage(message.channel, s"Sorry, I could not query the databank")
      }

      publish(response)
  }
}