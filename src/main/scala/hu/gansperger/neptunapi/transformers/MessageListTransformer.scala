package hu.gansperger.neptunapi.transformers

import java.util.regex.Pattern
import hu.gansperger.neptunapi.MessageListType
import hu.gansperger.neptunapi.data.NeptunMailPreview
import org.jsoup.nodes.{Element, Document}
import scala.collection.JavaConverters._
import hu.gansperger.neptunapi.Implicits._

object MessageListTransformer {
  def apply(doc : Document) = new MessageListTransformer(doc)
}

class MessageListTransformer(doc : Document) extends Transformer[MessageListType] {
  protected[this] val readMsg = "Elolvasott üzenet"
  protected[this] val elementId = "c_messages_gridMessages_bodytable"
  protected[this] val pattern = ("id" ,Pattern.compile("tr__[0-9]+"))

  protected[this] def parseMail(raw: Element): NeptunMailPreview =
  {
    val id = raw.id().substring(4)
    val read = raw.getElementsByTag("img").last().attr("alt") match {
      case `readMsg` => true
      case _ => false
    }
    val subject = raw.children().get(6).text()
    val sender = raw.children().get(4).text()
    val date = raw.children().get(7).text().toDate

    NeptunMailPreview(id, read, subject, sender, date)
  }

  override def transform : MessageListType = {
    val table = doc.getElementById(elementId)
    table.getElementsByAttributeValueMatching(pattern._1, pattern._2)
      .iterator()
      .asScala
      .map(parseMail)
      .toSeq
  }
}
