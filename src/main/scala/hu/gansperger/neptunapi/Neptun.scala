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
  def apply(requestHandler: NeptunRequestHandler,
            responseHandler: NeptunResponseHandler) =
    new Neptun[LoginableState](requestHandler, responseHandler)
}

class Neptun[T <: NeptunState] private (requestHandler: NeptunRequestHandler,
                                        responseHandler: NeptunResponseHandler) {

  private[this] val session = requestHandler.session
  private[this] val URL = session.URL

  private[this] def addCookies(req: Req) = session.cookies.foldLeft(req) {
      case (r, c) =>
        r.addCookie(c)
  }

  private[this] def addUserAgent(req: Req) =
    req <:< Map(("User-Agent","Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:27.0) Gecko/20100101 Firefox/27.0"))

  def login(neptunCode: String, password: String)(implicit ev: T  <:< LoginableState): Future[Neptun[LoggedInState]] = {
    val PostStringMessage(path, body) = requestHandler.login(neptunCode, password)
    val myReq =
      host(s"$URL/$path").POST.secure <:< Map(("Content-Type","application/json; charset=utf-8")) << body

    for {
      (respBody, cookies) <- Http(myReq OK as.Response(x =>
        (x.getResponseBody.replaceAll("\\\\u0027", "\'"), x.getCookies.asScala)))
    } yield {
      val error = ".*success:\'(True|False)\'.*".r.findFirstMatchIn(respBody).map(_.group(1))
      val errorMsg = ".*errormessage:\'(.*?)\'.*".r.findFirstMatchIn(respBody).map(_.group(1))

      error match {
        case Some("True") =>
          new Neptun[LoggedInState](requestHandler.addCookies(cookies.toList), responseHandler)
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

  def callMain()(implicit ev: T <:< LoggedInState) : Future[Neptun[MainCalledState]] = {
      val GetMessage(path, _) = requestHandler.getMain
      val myReq = addCookies(addUserAgent(host(URL) / path).setFollowRedirects(true).secure)
      Http(myReq OK as.Response(_ => new Neptun[MainCalledState](requestHandler, responseHandler)))
  }

  def fetchMails(page: Int)(implicit ev: T <:< MainCalledState) : Future[MessageListType] = {
      val GetMessage(path, parameters) = requestHandler.getMessagesPage(page)
      val myReq = addCookies(addUserAgent(host(URL) / path)).setFollowRedirects(true).secure <<? parameters
      Http(myReq OK as.Response(responseHandler.handleMessages))
  }

  def getSingleMail(id: Int)(implicit ev: T <:< MainCalledState) : Future[MessageType] = {
    val PostMessage(path, parameters) = requestHandler.getSingleMail(id)
    val myReq = addUserAgent(addCookies(host(s"$URL/$path"))).POST.secure <:< Map(("X-Requested-With","XMLHttpRequest"),
      ("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")) << parameters
    Http(myReq OK as.Response(responseHandler.handleSingleMail))
  }

}