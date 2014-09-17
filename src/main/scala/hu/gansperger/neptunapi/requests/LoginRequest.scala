package hu.gansperger.neptunapi.requests

import dispatch.{as, Http, Future}
import hu.gansperger.neptunapi.{LoginException, Session}
import hu.gansperger.neptunapi.constants.{Payload, URL}
import scala.collection.JavaConverters._

class LoginRequest(neptunCode : String,
                   password : String)(
                   override implicit val session : Session) extends SecureRequest[Session] {

  protected[this] val successPattern = ".*success:\'(True|False)\'.*".r
  protected[this] val errorPattern = ".*errormessage:\'(.*?)\'.*".r

  override def request() : Future[Session] = {
    val request =
      (baseRequest / URL.login)
        .setContentType("application/json", "charset=utf-8")
        .POST << Payload.login(neptunCode, password)

    for {
      (respBody, cookies) <-
        Http(request OK as.Response(x =>
          (x.getResponseBody.replaceAll("\\\\u0027", "\'"), x.getCookies.asScala)))
    } yield {
      val error = successPattern.findFirstMatchIn(respBody).map(_.group(1))
      val errorMsg = errorPattern.findFirstMatchIn(respBody).map(_.group(1))

      error match {
        case Some("True") =>
          session.copy(cookies = cookies.toList)
        case Some("False") =>
          throw new LoginException(errorMsg.get)
        case _ =>
          throw new LoginException("Unknown error.")
      }
    }
  }
}
