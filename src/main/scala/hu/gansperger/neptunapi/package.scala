package hu.gansperger

import com.ning.http.client.cookie.Cookie
import java.io.{PrintWriter, BufferedOutputStream, File}
import com.github.nscala_time.time.Imports._

package object neptunapi {
  implicit def string2Date: (String) => DateTime = DateTimeFormat.forPattern("yyyy.MM.dd. HH:mm:ss").parseDateTime

  case class Session(URL: String, cookies: List[Cookie])
  class NeptunException(reason: String) extends Exception(reason) { }
  class LoginException(reason: String) extends NeptunException(reason) { }

  val defNeptun = Neptun[LoginableState](new ElteNeptun(Session("hallgato.neptun.elte.hu", Nil)))

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
