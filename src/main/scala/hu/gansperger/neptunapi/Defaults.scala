package hu.gansperger.neptunapi

object Defaults {
  val login: (Any*) => PostMessage = PostMessageFactory("login.aspx/CheckLoginEnable",
    """{"user":"%s","pwd":"%s","UserLogin":null, "GUID":null}""") _

  val getMessages = GetMessageFactory("main.aspx", Map(("ctrl", "inbox")))

  val getMain = GetMessageFactory("main.aspx", Map.empty)

  val savePopupState: (Any*) => PostMessage = PostMessageFactory("main.aspx/SavePopupState",
    """{"state":"%s","PopupID":"%s"}""") _

  ///TODO delete this shit
  val defNeptun = Neptun("hallgato.neptun.elte.hu")

}
