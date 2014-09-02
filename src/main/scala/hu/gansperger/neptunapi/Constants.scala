package hu.gansperger.neptunapi

object Constants {
  object URL {
    val main        = "main.aspx"
    val login       = "login.aspx/CheckLoginEnable"
    val messagePage = "HandleRequest.ashx"
  }

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

  object Message {
    val gridId = "c_messages_gridMessages"
    val requestType = "GetData"
    val fixedHeader = false
    val searchCol = ""
    val searchText = ""
    val searchExp = false
    val allowSubRows = false

    val eventTarget = "upFunction$c_messages$upMain$upGrid$gridMessages"
    val eventArg = (id : Int) => s"commandname=;commandsource=select;id=$id;level=1"
  }

  object Request {
    val requestType  = (typ : String) => ("RequestType", typ)
    val gridId       = (id : String) => ("GridID", "c_messages_gridMessages")
    val pageIndex    = (page : Int) => ("pageindex", page.toString)
    val pageSize     = (pagesize : Int) => ("pagesize", pagesize.toString)
    val sort1        = (sort : String) => ("sort1", sort)
    val sort2        = (sort : String) => ("sort2", sort)
    val fixedHeader  = (header : Boolean) => ("fixedheader", header.toString)
    val searchCol    = (col : String) => ("searchcol", col)
    val searchText   = (text : String) => ("searchtext", text)
    val searchExp    = (expanded : Boolean) => ("searchexpanded", expanded.toString)
    val allowSubRows = (allow : Boolean) => ("allsubrowsexpanded", allow.toString)
  }

  val sendDate = "SendDate"

  val sortDesc = (field : String) => s"$field DESC"
  val sortAsc = (field : String) => s"$field Asc"
}
