package hu.gansperger.neptunapi

import dispatch._
import scala.concurrent.ExecutionContext.Implicits.global

object Neptun {
  def apply(requestHandler: NeptunRequestHandler,
            responseHandler: NeptunResponseHandler) =
    new Neptun[LoginableState](requestHandler, responseHandler)
}

class Neptun[T <: NeptunState] private (requestHandler: NeptunRequestHandler,
                                        responseHandler: NeptunResponseHandler) {

  def login(neptunCode: String, password: String)
           (implicit ev: T  <:< LoginableState): Future[Neptun[LoggedInState]] = {
    for {
      session <- requestHandler.login(neptunCode, password)
    } yield new Neptun[LoggedInState](requestHandler.withSession(session), responseHandler)
  }

  def callMain()(implicit ev: T <:< LoggedInState) : Future[Neptun[MainCalledState]] = {
      requestHandler
        .getMain()
        .map(_ => new Neptun[MainCalledState](requestHandler, responseHandler))
  }

  def fetchMails(page: Int)(implicit ev: T <:< MainCalledState) : Future[MessageListType] = {
      for {
        doc <- requestHandler.getMessagesPage(page)
      } yield responseHandler.handleMessages(doc)
  }

//  def getSingleMail(id: Int)(implicit ev: T <:< MainCalledState) : Future[MessageType] = {
//    val PostMessage(path, parameters) = requestHandler.getSingleMail(id)
//    val myReq = addUserAgent(addCookies(host(s"$URL/$path"))).POST.secure <:< Map(("X-Requested-With","XMLHttpRequest"),
//      ("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")) << parameters
//    Http(myReq OK as.Response(responseHandler.handleSingleMail))
//  }

}