package hu.gansperger

import com.ning.http.client.cookie.Cookie
import java.io.{PrintWriter, File}

import hu.gansperger.neptunapi.data.{NeptunMailPreview, NeptunMail}

package object neptunapi {
  import hu.gansperger.neptunapi.Implicits._

  def ignore[T](a : T) = ()

  type MessageListType = Seq[NeptunMailPreview]
  type MessageType = NeptunMail

  case class Session(URL: String, cookies: List[Cookie], userAgent : String)
  class NeptunException(reason: String) extends Exception(reason) { }
  class LoginException(reason: String) extends NeptunException(reason) { }

  val defNeptun = Neptun(new ElteRequestHandler(), new DefaultResponseHandler())

  def logToFile(contents: String): Unit = {
    val log = new File("log.html")
    val sw = new PrintWriter(log)
    try {
      sw.write(contents)
    }
    finally {
      sw.close()
    }

  }

}
