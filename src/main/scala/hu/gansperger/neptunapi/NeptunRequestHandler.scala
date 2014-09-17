package hu.gansperger.neptunapi

import com.ning.http.client.Response
import dispatch.Future
import hu.gansperger.neptunapi.constants.MessageList._
import hu.gansperger.neptunapi.constants._
import hu.gansperger.neptunapi.requests.{MainRequest, MessageListRequest, LoginRequest}

import scala.concurrent.ExecutionContext.Implicits.global

trait NeptunRequestHandler {
  protected[this] val session : Session

  protected[this] val defaultPageSize : Int
  protected[this] val defaultSort1 : String
  protected[this] val defaultSort2 : String

  def login(neptunCode: String, password: String) : Future[Session]

  def getMain() : Future[Unit]

  def getMessagesPage(page: Int,
                      pageSize: Int = defaultPageSize,
                      sort1: String = defaultSort1,
                      sort2: String = defaultSort2): Future[Response]
//
//  def getSingleMail(id: Int): PostMessage
//
//  val getMain: GetMessage
//  //val savePopupState: (Any*) => PostMessage

  def withSession(session : Session) : NeptunRequestHandler
}

class DefaultRequestHandler(override val session : Session) extends NeptunRequestHandler {
  private[this] implicit val s = session

  override protected[this] val defaultPageSize = 100
  override protected[this] val defaultSort1 = sortDesc(sendDate)
  override protected[this] val defaultSort2 = ""

  override def login(neptunCode : String, password : String) =
    LoginRequest(neptunCode, password).request()

  override def getMain() : Future[Unit] =
    MainRequest().request()

  override def getMessagesPage(page: Int,
                               pageSize: Int = defaultPageSize,
                               sort1: String = defaultSort1,
                               sort2: String = defaultSort2): Future[Response] =
    MessageListRequest(page, pageSize, sort1, sort2).request()

  override def withSession(session : Session) =
    new DefaultRequestHandler(session)
}

class ElteRequestHandler() extends DefaultRequestHandler(Session("hallgato.neptun.elte.hu", Nil, Misc.userAgent))