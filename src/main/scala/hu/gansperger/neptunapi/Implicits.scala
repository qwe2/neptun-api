package hu.gansperger.neptunapi

import com.github.nscala_time.time.Imports._

object Implicits {
  implicit class DateTransformer(s : String) {
    private[this] val format = "yyyy.MM.dd. HH:mm:ss"

    def toDate : DateTime =
      DateTimeFormat.forPattern(format).parseDateTime(s)

    def toDateOption : Option[DateTime] =
      DateTimeFormat.forPattern(format).parseOption(s)
  }
}
