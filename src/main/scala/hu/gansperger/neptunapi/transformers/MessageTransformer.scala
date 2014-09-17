package hu.gansperger.neptunapi.transformers

import hu.gansperger.neptunapi.MessageType
import hu.gansperger.neptunapi.data.NeptunMail
import org.jsoup.nodes.Document
import hu.gansperger.neptunapi.Implicits._

object MessageTransformer {
  def apply(doc : Document) = new MessageTransformer(doc)
}

class MessageTransformer(doc : Document) extends Transformer[MessageType] {
  protected[this] val rowDataId = (row : Int) => s"ntbMessage_row${row}dataTitleValue"
  protected[this] val rowDataClass = "tableRowData"
  protected[this] val readId = "Readmessage1_lblMessage"

  protected[this] val recId = "upFunction_c_messages_upModal_upmodalextenderReadMessage_ctl02_Readmessage1_UpdatePanel1_upNTable_spanMessageTo"

  protected[this] def getRow(row : Int) =
    doc.getElementById(rowDataId(row)).getElementsByClass(rowDataClass).text()

  override def transform : MessageType = {
    val subject = getRow(0)
    val sender = getRow(1)
    val sendDate = getRow(2).toDate
    val dueDate = getRow(3).toDateOption
    val receivers = doc.getElementById(recId).text().split(";").map(_.trim).toList
    val isMore = doc.getElementById("Readmessage1_linkMegmutat") != null

    val message = doc.getElementById(readId).html()

    NeptunMail(subject, sender, sendDate, dueDate, receivers, message, isMore)
  }
}
