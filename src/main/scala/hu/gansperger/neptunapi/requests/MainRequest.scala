package hu.gansperger.neptunapi.requests

import dispatch.{Http, as, Future}
import hu.gansperger.neptunapi.constants.URL
import hu.gansperger.neptunapi.{Session, ignore}

class MainRequest(override val session : Session) extends SecureRequest[Unit] {
  override def request() : Future[Unit] = {
    val request =
      (baseRequest / URL.main).setFollowRedirects(true)

    Http(request OK as.Response(ignore))
  }
}
