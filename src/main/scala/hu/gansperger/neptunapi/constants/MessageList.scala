package hu.gansperger.neptunapi.constants

object MessageList {
  val sendDate = "SendDate"

  val sortDesc = (field : String) => s"$field DESC"
  val sortAsc = (field : String) => s"$field Asc"
}
