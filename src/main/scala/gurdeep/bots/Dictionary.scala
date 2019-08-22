package gurdeep.bots

import scala.util.Random

object Dictionary {
  val dict = Map(
    "protocol" -> "an official procedure or system of rules governing affairs",
    "ip address" -> "a numerical label assigned to each device connected to a computer network that uses the Internet Protocol for communication",
    "digital signal" -> "data, represented as a sequence of discrete values",
    "port" -> "a logical construct that identifies a specific process or a type of network service"
  )

  def getOne = {
    if(dict.size == 0) None
    else {
      val idx = Random.nextInt(dict.size)
      val tips = dict.toArray
      Some(tips(idx))
    }
  }
  def get(key: String) = dict.get(key).getOrElse(
    s"""Hmm, I cannot find anything about $key.
      |I'll see if I should add this to the databank""".stripMargin)
}
