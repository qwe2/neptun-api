package hu.gansperger.neptunapi.requests

import dispatch.{Http, as, Future}
import hu.gansperger.neptunapi.constants.URL
import hu.gansperger.neptunapi.{Session, ignore}

import scala.concurrent.ExecutionContext.Implicits.global

object MainRequest {
  def apply()(implicit session : Session) =
    new MainRequest()
}

class MainRequest()(override implicit val session : Session) extends SecureRequest[Unit] {
  override def request() : Future[Unit] = {
    val request =
      (baseRequest / URL.main).setFollowRedirects(true)

    Http(request OK as.Response(ignore))
  }
}
