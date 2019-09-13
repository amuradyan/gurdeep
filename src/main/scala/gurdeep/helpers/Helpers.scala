package gurdeep.helpers

import java.util.concurrent.TimeUnit

import com.google.gson.Gson
import gurdeep.bots.{Definition, Fact}
import org.mongodb.scala._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by spectrum on 5/4/2018.
  */
object Helpers {

  implicit class DocumentObservable[C](val observable: Observable[Document]) extends ImplicitObservable[Document] {
    override val converter: (Document) => String = (doc) => doc.toJson
  }

  implicit class DefinitionObservable[C](val observable: Observable[Definition]) extends ImplicitObservable[Definition] {
    val gson = new Gson();
    override val converter: (Definition) => String = (doc) => gson.toJson(doc)
  }

  implicit class FactObservable[C](val observable: Observable[Fact]) extends ImplicitObservable[Fact] {
    val gson = new Gson();
    override val converter: (Fact) => String = (doc) => gson.toJson(doc)
  }

  implicit class GenericObservable[C](val observable: Observable[C]) extends ImplicitObservable[C] {
    override val converter: (C) => String = (doc) => doc.toString
  }

  trait ImplicitObservable[C] {
    val observable: Observable[C]
    val converter: (C) => String

    def results(): Seq[C] = Await.result(observable.toFuture(), Duration(10, TimeUnit.SECONDS))

    def headResult() = Await.result(observable.head(), Duration(10, TimeUnit.SECONDS))

    def printResults(initial: String = ""): Unit = {
      if (initial.length > 0) print(initial)
      results().foreach(res => println(converter(res)))
    }

    def printHeadResult(initial: String = ""): Unit = println(s"${initial}${converter(headResult())}")
  }
}
