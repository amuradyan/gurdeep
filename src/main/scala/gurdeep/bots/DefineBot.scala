package gurdeep.bots

import io.scalac.slack.MessageEventBus
import io.scalac.slack.bots.AbstractBot
import io.scalac.slack.common.{Command, OutboundMessage}

class DefineBot(override val bus: MessageEventBus) extends AbstractBot {

  override def help(channel: String): OutboundMessage =
    OutboundMessage(channel,
      s"$name will help you to solve difficult math problems \n" +
      "Usage: $def {operation} {arguments separated by space}")

  override def act: Receive = {
    case Command("def", term_parts, message) if term_parts.size != 0 => {
      val term = term_parts.mkString("")
      val response = OutboundMessage(message.channel, Dictionary.get(term))
      publish(response)
    }
    case Command("def", _, message) =>
      publish(OutboundMessage(message.channel, s"You have not specified the term you want defined!"))
  }
}