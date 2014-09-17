package hu.gansperger.neptunapi.constants

object Payload {
  def login(neptunCode : String, password : String) =
    s"""{"user":"$neptunCode","pwd":"$password","UserLogin":null, "GUID":null}"""

  def eventTarget(eventtarget : String, eventarg : String) =
    Map(
      "__EVENTTARGET"     -> eventtarget,
      "__EVENTARGUMENT"   -> eventarg,
      "__VIEWSTATE"       -> "",
      "__EVENTVALIDATION" -> "",
      "__LASTFOCUS"       -> "",
      "__ASYNCPOST"       -> "true"
    )
}
