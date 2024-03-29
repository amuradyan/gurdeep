package gurdeep.data

import com.mongodb.ConnectionString
import com.typesafe.config.ConfigFactory
import gurdeep.bots._
import gurdeep.helpers.Helpers._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.{MongoClient, MongoClientSettings, MongoCollection, MongoCredential}
import scalaj.http._

object Databank {
  import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
  import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
  import org.mongodb.scala.bson.codecs.Macros._

  val conf = ConfigFactory.load()

  val db = conf.getString("mongodb.db")
  val user = conf.getString("mongodb.user")
  val password = conf.getString("mongodb.password")
  val host = conf.getString("mongodb.host")
  val port = conf.getString("mongodb.port")


  val clientSettingsBuilder = MongoClientSettings.builder()
  val mongoConnectionString = new ConnectionString(s"mongodb://$host:$port")

  val credential = MongoCredential.createScramSha1Credential(user, db, password.toCharArray)
  clientSettingsBuilder.credential(credential)
  clientSettingsBuilder.applyConnectionString(mongoConnectionString)

  val codecRegistry = fromRegistries(
    fromProviders(
      classOf[Definition],
      classOf[Fact],
      classOf[Section],
      classOf[Term],
      classOf[Tags]
    ),
    DEFAULT_CODEC_REGISTRY)

  val mongoClient = MongoClient(clientSettingsBuilder.build())

  val gurdeepDB = mongoClient.getDatabase(db).withCodecRegistry(codecRegistry)
  val glossary: MongoCollection[Definition] = gurdeepDB.getCollection("glossary")
  val facts: MongoCollection[Fact] = gurdeepDB.getCollection("facts")

  private def getTechTermsURL(term: String) = {
    val referenceURL = s"https://techterms.com/definition/${term}"
    val response = Http(referenceURL).asString

    if (response.code == 200) referenceURL else ""
  }

  private def getTechTermReference (term: String)= {
    val techTermsURL = getTechTermsURL(term)
    if(techTermsURL.nonEmpty) s"See more at: $techTermsURL" else ""
  }

  def define(term: String) = {
    glossary.find(equal("term", term)).results().headOption match {
      case Some(definition) => {
        val techTermsReference = getTechTermReference(term)
        s"${definition.asDefinition}\\n${techTermsReference}"
      }
      case None =>
        s"Hmm, I cannot find anything about $term. I'll see if I should add this to the databank"
    }
  }

  def randomFact = facts.aggregate(Seq(sample(1))).results().headOption match {
    case Some(fact) => fact.fact
    case None =>
        "Strange, but I was unable to find any facts in databank.\\n" +
        "This incident will be reported and taken care of."
  }

  def randomDefinition = glossary.aggregate(Seq(sample(1))).results().headOption match {
    case Some(definition) => {
      val techTermsReference = getTechTermReference(definition.term)
      s"${definition.asDefinition}\\n${techTermsReference}"
    }
    case None =>
        "It seems there are no definitions in my databank.\\n" +
        "This wont go unnoticed."
  }

  def list(section: Option[String]) = {
    val terms = glossary.withDocumentClass[Term]()
    val fieldsOfInterest = fields(
      excludeId(),
      include("term")
    )

    section match {
      case None => terms.find().projection(fieldsOfInterest ).results()
      case Some(section) => terms.find(regex("term", s"^$section", "i")).projection(include("term")).results()
    }
  }

  def getAllTags = {
    val tags = glossary.withDocumentClass[Tags]()
    val fieldsOfInterest = fields(
      excludeId(),
      include("tags")
    )

    tags.find.projection(fieldsOfInterest).results.foldLeft(Set[String]()){ (acc, t) => acc ++ t.tags }
  }

  def listTags(tags: List[String]) = {
    val filters = or(tags.map(equal("tags", _)): _*)
    val terms = glossary.find(filters).results
    terms.map(_.term.capitalize)
  }
}
