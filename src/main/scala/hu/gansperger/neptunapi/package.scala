package hu.gansperger

import com.ning.http.client.cookie.Cookie
import java.io.{PrintWriter, File}

package object neptunapi {
  import hu.gansperger.neptunapi.Implicits._

  type MessageListType = Seq[NeptunMailPreview]
  type MessageType = NeptunMail

  case class Session(URL: String, cookies: List[Cookie])
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
