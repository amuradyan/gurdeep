package gurdeep.bots

import gurdeep.data.Databank
import io.scalac.slack.MessageEventBus
import io.scalac.slack.bots.AbstractBot
import io.scalac.slack.common.{Command, OutboundMessage}

case class Tags(tags: List[String])

class TagBot(override val bus: MessageEventBus) extends AbstractBot {
  override def help(channel: String): OutboundMessage =
    OutboundMessage(channel, s"$name helps you browse the dictionary using tags.")

  override def act: Receive = {
    case Command("tags", _, message) => {
      val query = Databank.getAllTags
      val result = if (query.isEmpty) "I am sorry but the databank is currently empty" else query.mkString(" ")
      val response = OutboundMessage(message.channel, result)

      publish(response)
    }
    case Command("tag", tags, message) if tags.nonEmpty => {
      val query = Databank.listTags(tags)
      val result = if (query.isEmpty) "No terms with mentioned tags were found" else query.mkString(" ")
      val response = OutboundMessage(message.channel, result)

      publish(response)
    }
  }
}
