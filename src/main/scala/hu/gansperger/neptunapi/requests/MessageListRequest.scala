package hu.gansperger.neptunapi.requests

import com.ning.http.client.Response
import dispatch.{as, Http, Future}
import hu.gansperger.neptunapi.Session
import hu.gansperger.neptunapi.constants.{URL, Message, Request => R }

import scala.concurrent.ExecutionContext.Implicits.global


object MessageListRequest {
  def apply(page : Int, pageSize : Int, sort1 : String, sort2 : String)(implicit session : Session) =
    new MessageListRequest(page, pageSize, sort1, sort2)
}

class MessageListRequest(page : Int,
                         pageSize : Int,
                         sort1 : String,
                         sort2 : String)(
                         override implicit val session : Session) extends SecureRequest[Response] {

  override def request() : Future[Response] = {
    val params =
      Map(
        R.requestType(Message.requestType),
        R.gridId(Message.gridId),
        R.pageIndex(page),
        R.pageSize(pageSize),
        R.sort1(sort1),
        R.sort2(sort2),
        R.fixedHeader(Message.fixedHeader),
        R.searchCol(Message.searchCol),
        R.searchText(Message.searchText),
        R.searchCol(Message.searchCol),
        R.allowSubRows(Message.allowSubRows)
      )

    val request =
      (baseRequest / URL.messagePage)
        .setFollowRedirects(true) <<? params

    Http(request OK as.Response(identity))
  }

}
