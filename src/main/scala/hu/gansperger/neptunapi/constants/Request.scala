package hu.gansperger.neptunapi.constants

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
