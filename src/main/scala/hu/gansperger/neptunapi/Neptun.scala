package hu.gansperger.neptunapi

import dispatch._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}
import play.api.libs.json.Json
import org.jsoup.Jsoup
import com.ning.http.client.Response
import java.util.regex.Pattern
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

object Neptun {
  def apply(session: Session) = new Neptun(session)
  def apply(URL: String) = new Neptun(URL)
}

class Neptun(session: Session) {
  def this(URL: String) = this(Session(URL, Nil))

  private def addCookies(req: Req) = session.cookies.foldLeft(req){
      case (r, c) =>
        r.addCookie(c)
  }

  private def addUserAgent(req: Req) =
    req <:< Map(("User-Agent","Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:27.0) Gecko/20100101 Firefox/27.0"))

  def login(neptunCode: String, password: String): Try[Neptun] = {
    Try {
      val PostMessage(path, body) = Defaults.login(neptunCode, password)
      val myReq =
        host(s"${session.URL}/$path").POST.secure <:< Map(("Content-Type","application/json; charset=utf-8")) << body

      val (respBody, cookies) = Http(myReq OK as.Response(
        x => (x.getResponseBody.replaceAll("\\\\u0027","\'"), x.getCookies.asScala))).apply()
      val error = ".*success:\'(True|False)\'.*".r.findFirstMatchIn(respBody).map(_.group(1))
      val errorMsg = ".*errormessage:\'(.*?)\'.*".r.findFirstMatchIn(respBody).map(_.group(1))

      error match {
        case Some("True") =>
          Neptun(Session(session.URL, cookies.toList))
        case Some("False") =>
          throw new LoginException(errorMsg.get)
        case _ =>
          throw new LoginException("Unknown error.")
      }
    }
  }

  def sendPopupState(): Try[Neptun] = {
    Try {
      val PostMessage(path, body) = Defaults.savePopupState("hidden",
        "upFunction_c_messages_upModal_modalextenderReadMessage")

      val myReq =
        addCookies(host(session.URL) / path).POST.secure <:< Map(("Content-Type","application/json; charset=utf-8")) << body

      Http(myReq OK as.Response(identity))

      this
    }
  }

  def callMain(): Try[Neptun] = {
    Try {
      val GetMessage(path, parameters) = Defaults.getMain

      val myReq = addCookies(addUserAgent(host(session.URL) / path).setFollowRedirects(true).secure)

      Http(myReq OK as.Response{x => logToFile(x.getResponseBody); x}).apply()

      this
    }

  }

  private def parseMail(raw: Element): NeptunMail = {
    val id = raw.id().substring(4)
    val read = raw.getElementsByTag("img").last().attr("alt") match {
      case "Elolvasott üzenet" => true
      case _ => false
    }
    val subject = raw.children().get(6).text()
    val sender = raw.children().get(4).text()
    val date = raw.children().get(7).text()

    NeptunMail(id, read, subject, sender, date)
  }

  def fetchMails() = {
    def messagesToList(itr: java.util.Iterator[Element], acc: List[NeptunMail]): List[NeptunMail] =
      if(itr.hasNext) {
        val next = itr.next()
        messagesToList(itr, acc :+ parseMail(next))
      }
      else acc

    val GetMessage(path, parameters) = Defaults.getMessages

    val myReq = addCookies(host(session.URL) / path).setFollowRedirects(true).secure <<? parameters

    val resp = Http(myReq OK as.Response{x =>
      logToFile(x.getResponseBody)
      val doc = Jsoup.parse(x.getResponseBody)
      val table = doc.getElementById("c_messages_gridMessages_bodytable")
      messagesToList(table.getElementsByAttributeValueMatching("id", Pattern.compile("tr__[0-9]+")).iterator(), Nil)
    }).apply()

    resp
  }


}
