package hu.gansperger.neptunapi

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
  override val defaultPageSize: Int = 100
  override val defaultSort1: String = "SendDate DESC"

  private[this] val mainPage = "main.aspx"

  override def login(neptunCode: String, password: String): PostStringMessage =
    PostStringMessage("login.aspx/CheckLoginEnable", s"""{"user":"$neptunCode","pwd":"$password","UserLogin":null, "GUID":null}""")

  override def getMessages: GetMessage = GetMessage(mainPage, Map.empty)

  override def getMessagesPage(page: Int, pageSize: Int = defaultPageSize, sort1: String = defaultSort1, sort2: String = defaultSort2): GetMessage =
    GetMessage("HandleRequest.ashx", Map(("RequestType","GetData"),
      ("GridID","c_messages_gridMessages"), ("pageindex", page.toString),
      ("pagesize", pageSize.toString), ("sort1", sort1), ("sort2", sort2), ("fixedheader","false"),
      ("searchcol", ""), ("searchtext", ""), ("searchexpanded","false"), ("allsubrowsexpanded","False")))

  override def getSingleMail(id: Int): PostMessage =
    PostMessage(mainPage, Map(("__EVENTTARGET", "upFunction$c_messages$upMain$upGrid$gridMessages"),
      ("__EVENTARGUMENT", s"commandname=;commandsource=select;id=$id;level=1"), ("__VIEWSTATE",""),
      ("__EVENTVALIDATION", ""), ("__LASTFOCUS", ""), ("__ASYNCPOST", "true")))

  override val getMain = GetMessage("main.aspx", Map.empty)

  /*override val savePopupState: (Any*) => PostMessage = PostMessageFactory("main.aspx/SavePopupState",
    """{"state":"%s","PopupID":"%s"}""") _*/

  ///TODO delete this shit
  val elteNeptun = new ElteNeptun(Session("hallgato.neptun.elte.hu", Nil))
  val defNeptun = Neptun(elteNeptun)
}
