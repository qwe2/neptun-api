package hu.gansperger.neptunapi.constants

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
