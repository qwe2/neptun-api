package hu.gansperger.neptunapi.transformers

import com.github.nscala_time.time.Imports._
import org.scalatest._
import hu.gansperger.neptunapi.Utils

class MessageListTransformerTest(
  val uniprefix : String,
  val sender : String,
  val lngth : Int,
  val date : String,
  val subj : String,
  val hread : Boolean,
  val thirdread : Boolean) extends FlatSpec with Matchers {

  val doc = Utils.loadFromResource(s"$uniprefix/messages/message_list.html")
  def formatter = DateTimeFormat.forPattern("yyyy.MM.dd. HH:mm:ss").parseDateTime _

  val result = MessageListTransformer(doc).transform

  "A MessageListTransformer" should "transform the right messages" in {
    result.head.sender should be (sender)
    result.tail.length should be (lngth)
    result(9).date should be (formatter(date))
    result.last.subject should be (subj)
    result.head.read should be (hread)
    result(2).read should be (thirdread)
  }
}

class ElteMessageListTransformerTest extends MessageListTransformerTest(
  "/elte",
  "Csillag András",
  19,
  "2014.07.22. 16:32:22",
  "A BBV-200.170/EC tárgyra jegybeírás történt!",
  false,
  true
) { }

class SoteMessageListTransformerTest extends MessageListTransformerTest(
  "/sote",
  "Pékli Andrea",
  19,
  "2014.08.27. 16:05:31",
  "11. Semmelweis Könyvhét (2014. szeptember 8-12.)",
  true,
  false
) { }
