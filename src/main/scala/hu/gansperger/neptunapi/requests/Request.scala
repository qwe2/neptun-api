package hu.gansperger.neptunapi.requests

import dispatch.{host, Future}
import hu.gansperger.neptunapi.Session
import hu.gansperger.neptunapi.Implicits._

trait Request[T] {
  def request() : Future[T]
}

trait SessionRequest[T] extends Request[T] {
  val session : Session
  val baseRequest =
      host(session.URL)
        .setUserAgent(session.userAgent)
        .addCookies(session.cookies)
}

trait SecureRequest[T] extends SessionRequest[T] {
  override val baseRequest = super.baseRequest.secure
}
