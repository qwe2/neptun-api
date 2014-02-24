package hu.gansperger

import com.ning.http.client.Cookie
import java.io.{PrintWriter, BufferedOutputStream, File}

package object neptunapi {
  case class Session(URL: String, cookies: List[Cookie])
  class LoginException(reason: String) extends Exception(reason) { }

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
