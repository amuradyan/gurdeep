package gurdeep.bots

import gurdeep.data.Databank
import io.scalac.slack.MessageEventBus
import io.scalac.slack.bots.AbstractBot
import io.scalac.slack.common.{Attachment, Command, OutboundMessage, RichMessageElement, RichOutboundMessage, Title}

case class Section(section: String)

class ListBot(override val bus: MessageEventBus) extends AbstractBot{
  override def help(channel: String): OutboundMessage =
      OutboundMessage(channel, s"$name will present you a random tech fact")

  override def act: Receive = {
    case Command("list", section, message) if section.length <= 1 => {
      val rawResult = Databank.list(section.headOption)
      
      val responseMessage = if (section.isEmpty)
        rawResult.map(_.term.take(1).capitalize).sorted.toSet.mkString(" ")
      else
        rawResult.map(_.term.capitalize).mkString(" ")

      val result = if (rawResult.isEmpty){
        if (section.isEmpty)
          "I am sorry but the databank is currently empty"
        else
          s"No terms were found in section ${section.head}"
      } else
        responseMessage

//      val richResponse = RichOutboundMessage(message.channel, List(Attachment(Title("test"))))
      val response = OutboundMessage(message.channel, result)

      publish(response)
    }
  }
}
