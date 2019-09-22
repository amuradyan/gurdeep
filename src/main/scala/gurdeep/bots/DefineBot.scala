package gurdeep.bots

import gurdeep.data.Databank
import io.scalac.slack.MessageEventBus
import io.scalac.slack.bots.AbstractBot
import io.scalac.slack.common.{Command, OutboundMessage}

case class Term(term: String)

case class Definition(term: String, definition: String, tags: List[String]) {
  def asDefinition = s"*${term.capitalize}*: ${definition.capitalize}\\n${tags.mkString(" ")}"
}

class DefineBot(override val bus: MessageEventBus) extends AbstractBot {
  override def help(channel: String): OutboundMessage =
    OutboundMessage(channel, s"$name will help you find definitions of technical")

  override def act: Receive = {
    case Command("def", term_parts, message) if term_parts.size != 0 => {
      val responseMsg = Databank.define(term_parts.mkString(" "))
      val response = OutboundMessage(message.channel, responseMsg)
      publish(response)
    }
    case Command("def", _, message) =>
      publish(OutboundMessage(message.channel, Databank.randomDefinition))
  }
}