package hu.gansperger.neptunapi

import hu.gansperger.neptunapi.Constants.Message

trait NeptunConfig {
  val defaultPageSize: Int = 20
  val defaultSort1: String = ""
  val defaultSort2: String = ""

  def login(neptunCode: String, password: String): PostStringMessage
  def getMessages: GetMessage
  def getMessagesPage(page: Int, pageSize: Int = defaultPageSize, sort1: String = defaultSort1, sort2: String = defaultSort2): GetMessage

  def getSingleMail(id: Int): PostMessage

  val getMain: GetMessage
  //val savePopupState: (Any*) => PostMessage
}

object DefaultConfig extends NeptunConfig {
  import Constants._

  override val defaultPageSize: Int = 100
  override val defaultSort1: String = sortDesc(sendDate)

  override def login(neptunCode: String, password: String): PostStringMessage =
    PostStringMessage(URL.login, Payload.login(neptunCode, password))

  override def getMessages: GetMessage = GetMessage(URL.main, Map.empty)

  override def getMessagesPage(page: Int,
                               pageSize: Int = defaultPageSize,
                               sort1: String = defaultSort1,
                               sort2: String = defaultSort2): GetMessage =
    GetMessage(URL.messagePage,
      Map(
        Request.requestType(Message.requestType),
        Request.gridId(Message.gridId),
        Request.pageIndex(page),
        Request.pageSize(pageSize),
        Request.sort1(sort1),
        Request.sort2(sort2),
        Request.fixedHeader(Message.fixedHeader),
        Request.searchCol(Message.searchCol),
        Request.searchText(Message.searchText),
        Request.searchCol(Message.searchCol),
        Request.allowSubRows(Message.allowSubRows)
      )

  override def getSingleMail(id: Int): PostMessage =
    PostMessage(URL.main, Payload.eventTarget(Message.eventTarget, Message.eventArg(id)))

  override val getMain = GetMessage(URL.main, Map.empty)

  /*override val savePopupState: (Any*) => PostMessage = PostMessageFactory("main.aspx/SavePopupState",
    """{"state":"%s","PopupID":"%s"}""") _*/

  ///TODO delete this shit
  val elteNeptun = new ElteNeptun(Session("hallgato.neptun.elte.hu", Nil))
  val defNeptun = Neptun(elteNeptun)
}
