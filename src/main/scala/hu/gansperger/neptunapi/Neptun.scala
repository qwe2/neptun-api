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

import scala.annotation.tailrec

object Neptun {
  def apply[T <: NeptunState](handler: NeptunHandler, neptunConfig: NeptunConfig = DefaultConfig) =
    new Neptun[T](handler, neptunConfig)
}

class Neptun[T <: NeptunState](val handler: NeptunHandler, neptunConfig: NeptunConfig = DefaultConfig) {

  private[this] val URL = handler.session.URL

  private[this] def addCookies(req: Req) = handler.session.cookies.foldLeft(req) {
      case (r, c) =>
        r.addCookie(c)
  }

  private[this] def addUserAgent(req: Req) =
    req <:< Map(("User-Agent","Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:27.0) Gecko/20100101 Firefox/27.0"))

  def login(neptunCode: String, password: String)(implicit ev: T  <:< LoginableState): Try[Neptun[LoggedInState]] = {
    Try {
      val PostStringMessage(path, body) = neptunConfig.login(neptunCode, password)
      val myReq =
        host(s"$URL/$path").POST.secure <:< Map(("Content-Type","application/json; charset=utf-8")) << body

      val (respBody, cookies) = Http(myReq OK as.Response(
        x => (x.getResponseBody.replaceAll("\\\\u0027","\'"), x.getCookies.asScala))).apply()
      val error = ".*success:\'(True|False)\'.*".r.findFirstMatchIn(respBody).map(_.group(1))
      val errorMsg = ".*errormessage:\'(.*?)\'.*".r.findFirstMatchIn(respBody).map(_.group(1))

      error match {
        case Some("True") =>
          new Neptun[LoggedInState](handler.addCookies(cookies.toList), neptunConfig)
        case Some("False") =>
          throw new LoginException(errorMsg.get)
        case _ =>
          throw new LoginException("Unknown error.")
      }
    }
  }

  /*def sendPopupState(): Try[Neptun] = {
    Try {
      val PostMessage(path, body) = Defaults.savePopupState("hidden",
        "upFunction_c_messages_upModal_modalextenderReadMessage")

      val myReq =
        addCookies(host(URL) / path).POST.secure <:< Map(("Content-Type","application/json; charset=utf-8")) << body

      Http(myReq OK as.Response(identity))

      this
    }
  }*/

  def callMain(implicit ev: T <:< LoggedInState) : Try[Neptun[MainCalledState]] = {
    Try {
      val GetMessage(path, _) = neptunConfig.getMain
      val myReq = addCookies(addUserAgent(host(URL) / path).setFollowRedirects(true).secure)
      Http(myReq OK as.Response{x => logToFile(x.getResponseBody); x}).apply()

      new Neptun[MainCalledState](handler, neptunConfig)
    }
  }

  def fetchMails(page: Int)(implicit ev: T <:< MainCalledState) : handler.MessageType = {
      val GetMessage(path, parameters) = neptunConfig.getMessagesPage(page)
      val myReq = addCookies(addUserAgent(host(URL) / path)).setFollowRedirects(true).secure <<? parameters
      Http(myReq OK as.Response(handler.handleMessages)).apply()
  }

  def getSingleMail(id: Int)(implicit ev: T <:< MainCalledState) : handler.MailType = {
    val PostMessage(path, parameters) = neptunConfig.getSingleMail(id)
    val myReq = addUserAgent(addCookies(host(s"$URL/$path"))).POST.secure <:< Map(("X-Requested-With","XMLHttpRequest"),
      ("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")) << parameters
    Http(myReq OK as.Response(handler.handleSingleMail)).apply()
  }

}
