package hu.gansperger.neptunapi

import org.jsoup.Jsoup

import scala.io.Source

object Utils {
  def loadFromResource(path : String) = {
    val res = Source.fromURL(getClass.getResource(path))
    val doc = res.mkString
    Jsoup.parse(doc)
  }
}
