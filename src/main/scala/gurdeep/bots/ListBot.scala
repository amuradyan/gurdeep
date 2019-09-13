package gurdeep.bots

import gurdeep.data.Databank
import io.scalac.slack.MessageEventBus
import io.scalac.slack.bots.AbstractBot
import io.scalac.slack.common.{Command, OutboundMessage}

case class Section(section: String)

class ListBot(override val bus: MessageEventBus) extends AbstractBot{
  override def help(channel: String): OutboundMessage =
      OutboundMessage(channel, s"$name will present you a random tech fact")

  override def act: Receive = {
    case Command("list", section, message) if section.isEmpty || section.length == 1 => {
      val query = Databank.list(section.headOption).foldLeft("") { (acc, el) => s"$acc ${el.section}" }
      val result = if (query.isEmpty) "I am sorry but the databank is currently empty" else query
      val response = OutboundMessage(message.channel, result)

      publish(response)
    }
  }
}
