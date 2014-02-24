package hu.gansperger.neptunapi

trait Message
case class PostMessage(path: String, body: String) extends Message
case class GetMessage(path: String, parameters: Map[String, String]) extends Message

object PostMessageFactory {
  def apply(path: String, pattern: String)(args: Any*) =
    new PostMessage(path, pattern format (args: _*))
}

object GetMessageFactory {
  def apply(path: String, keyValues: Map[String,String]) =
    new GetMessage(path, keyValues)
}


