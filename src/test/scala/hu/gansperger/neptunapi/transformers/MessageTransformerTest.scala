package hu.gansperger.neptunapi.transformers

import com.github.nscala_time.time.Imports._
import org.scalatest._
import hu.gansperger.neptunapi.Utils

class MessageTransformerTest(
  val uniprefix : String,

  val doc1subject : String,
  val doc1sender : String,
  val doc1senddate : String,
  val doc1contain1 : String,
  val doc1contain2 : String,
  val doc1receivers : List[String],

  val doc2duedate : String,
  val more : Boolean) extends FlatSpec with Matchers {

  val doc = Utils.loadFromResource(s"$uniprefix/messages/message.html")
  val doc2 = Utils.loadFromResource(s"$uniprefix/messages/message2.html")
  def formatter = DateTimeFormat.forPattern("yyyy.MM.dd. HH:mm:ss").parseDateTime _

  "A MessageTransformer" should "transform the right message" in {
    val result = MessageTransformer(doc).transform

    result.subject should be (doc1subject)
    result.sender should be (doc1sender)
    result.sendDate should be (formatter(doc1senddate))
    result.receivers should be (doc1receivers)

    result.message.contains(doc1contain1) should be (true)
    result.message.contains(doc1contain2) should be (true)
    result.isMore should be (more)
  }

  "A message with no due date" should "have no due date" in {
    val result = MessageTransformer(doc).transform
    result.dueDate.isDefined should be (false)
  }

  "A message with due date" should "have the right value" in {
    val result = MessageTransformer(doc2).transform
    result.dueDate.isDefined should be (true)
    result.dueDate.get should be (formatter(doc2duedate))
  }
}

class ElteMessageTransformerTest extends MessageTransformerTest(
  "/elte",

  "EIK Kortárs Segítő Csoport- toborzás",
  "Csillag András",
  "2014.09.06. 12:17:32",
  "Kedves Hallgat&oacute;k!",
  "l&eacute;gy r&eacute;sze egy igazi",
  List("Gansperger István Márk"),

  "2014.09.19. 0:00:00",
  false
) { }

class SoteMessageTransformerTest extends MessageTransformerTest(
  "/sote",

  "Új választható tantárgy!",
  "Zsiskó Andrea",
  "2014.08.29. 11:31:25",
  "Az extracellul&aacute;ris vezikul&aacute;k szerepe a sejtek k&ouml;zti",
  "Az előad&aacute;sok helye &eacute;s kezd&eacute;si időpontja:",
  List("Szabó Kata", "Kurucz Liliána", "Záborszky Péter", "Paszerbovics Krisztián", "Czutor Levente"),

  "2014.10.05. 0:00:00",
  true
) { }