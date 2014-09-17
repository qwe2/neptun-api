package hu.gansperger.neptunapi.requests

import com.ning.http.client.Response
import dispatch.{as, Http, Future}
import hu.gansperger.neptunapi.Session
import hu.gansperger.neptunapi.constants.MessageList._
import hu.gansperger.neptunapi.constants.{URL, Message, Request}

class MessageListRequest(page : Int,
                         pageSize : Int = 100,
                         sort1 : String = sortDesc(sendDate),
                         sort2 : String = "")(
                         override implicit val session : Session) extends SecureRequest[Response] {

  override def request() : Future[Response] = {
    val params =
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

    val request =
      (baseRequest / URL.messagePage)
        .setFollowRedirects(true) <<? params

    Http(request OK as.Response(identity))
  }

}
