package hu.gansperger.neptunapi

import com.ning.http.client.{Cookie, Response}
import org.jsoup.nodes.Element
import java.util.regex.Pattern
import org.jsoup.Jsoup
import scala.annotation.tailrec

trait NeptunHandler {
  val session: Session

  type MessageType <: AnyRef
  type MailType <: AnyRef

  def addCookies(cookies: List[Cookie]): NeptunHandler
  def handleMessages(response: Response): MessageType
  def handleSingleMail(response: Response): MailType
}

class BaseNeptun(val session: Session) extends NeptunHandler {
  type MessageType = List[NeptunMailPreview]
  type MailType = NeptunMail

  private[this] def parseMail(raw: Element): NeptunMailPreview = {
    val id = raw.id().substring(4)
    val read = raw.getElementsByTag("img").last().attr("alt") match {
      case "Elolvasott üzenet" => true
      case _ => false
    }
    val subject = raw.children().get(6).text()
    val sender = raw.children().get(4).text()
    val date = raw.children().get(7).text()

    NeptunMailPreview(id, read, subject, sender, date)
  }

  override def addCookies(cookies: List[Cookie]): NeptunHandler = new ElteNeptun(Session(session.URL, cookies))

  override def handleMessages(response: Response): MessageType = {
    @tailrec
    def messagesToList(itr: java.util.Iterator[Element], acc: MessageType): MessageType =
      if(itr.hasNext) {
        val next = itr.next()
        messagesToList(itr, acc :+ parseMail(next))
      }
      else acc

    val doc = Jsoup.parse(response.getResponseBody)
    val table = doc.getElementById("c_messages_gridMessages_bodytable")
    messagesToList(
      table.getElementsByAttributeValueMatching("id", Pattern.compile("tr__[0-9]+")).iterator(), Nil)
  }

  override def handleSingleMail(response: Response): MailType = {
    val doc = Jsoup.parse(response.getResponseBody)

    logToFile(doc.toString)
    val subject = doc.getElementById("ntbMessage_row0dataTitleValue").getElementsByClass("tableRowData").text()
    val sender = doc.getElementById("ntbMessage_row1dataTitleValue").getElementsByClass("tableRowData").text()
    val sendDate = doc.getElementById("ntbMessage_row2dataTitleValue").getElementsByClass("tableRowData").text()
    val dueDate = doc.getElementById("ntbMessage_row3dataTitleValue").getElementsByClass("tableRowData").text()
    val receivers = doc.getElementById("ntbMessage_row4dataTitleValue").getElementsByClass("tableRowData").text()

    val message = doc.getElementById("Readmessage1_lblMessage").html()

    NeptunMail(subject, sender, sendDate, dueDate, receivers, message)
  }
}

class ElteNeptun(override val session: Session) extends BaseNeptun(session) { }
