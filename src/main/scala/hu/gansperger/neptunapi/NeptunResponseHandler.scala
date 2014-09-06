package hu.gansperger.neptunapi

import com.ning.http.client.Response
import org.jsoup.Jsoup
import hu.gansperger.neptunapi.transformers.{MessageTransformer, MessageListTransformer}

trait NeptunResponseHandler {
  def handleMessages(response: Response): MessageListType
  def handleSingleMail(response: Response): MessageType
}

class DefaultResponseHandler() extends NeptunResponseHandler {

  override def handleMessages(response: Response): MessageListType = {
    val doc = Jsoup.parse(response.getResponseBody)
    MessageListTransformer(doc).transform
  }

  override def handleSingleMail(response: Response): MessageType = {
    val doc = Jsoup.parse(response.getResponseBody)
    MessageTransformer(doc).transform
  }
}