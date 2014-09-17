package hu.gansperger.neptunapi

import com.github.nscala_time.time.Imports._
import com.ning.http.client.cookie.Cookie
import dispatch.Req

object Implicits {
  implicit class DateTransformer(s : String) {
    private[this] val format = "yyyy.MM.dd. HH:mm:ss"

    def toDate : DateTime =
      DateTimeFormat.forPattern(format).parseDateTime(s)

    def toDateOption : Option[DateTime] =
      DateTimeFormat.forPattern(format).parseOption(s)
  }

  implicit class RichReq(req : Req) {
    def addCookies(cookies : TraversableOnce[Cookie]) : Req = {
      cookies.foldLeft(req) {
        case (r, c) => r.addCookie(c)
      }
    }

    def setUserAgent(ua : String) : Req = {
      req <:< Map("UserAgent" -> ua)
    }
  }
}
