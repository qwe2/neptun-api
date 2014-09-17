package hu.gansperger.neptunapi.requests

import com.ning.http.client.Response
import dispatch.{as, Http, Future}
import hu.gansperger.neptunapi.Session
import hu.gansperger.neptunapi.constants.{Message, Payload, URL}

import scala.concurrent.ExecutionContext.Implicits.global

class MessageRequest(id : Int)(
                     override implicit val session : Session) extends SecureRequest[Response] {

  override def request() : Future[Response] = {
    val request =
      (baseRequest / URL.main)
        .setContentType("application/x-www-form-urlencoded", "utf-8")
        .POST << Payload.eventTarget(Message.eventTarget, Message.eventArg(id))

    Http(request OK as.Response(identity))
  }
}
