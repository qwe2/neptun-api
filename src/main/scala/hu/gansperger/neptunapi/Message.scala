package hu.gansperger.neptunapi

trait Message
case class PostStringMessage(path: String, body: String) extends Message
case class PostMessage(path: String, body: Map[String, String]) extends Message
case class GetMessage(path: String, parameters: Map[String, String]) extends Message

/*object PostMessageFactory {
  def apply(path: String, parameters: List[String])(args: String*) =
    new PostMessage(path, (parameters zip args).toMap)
  def apply(path: String, pattern: String)(args: String*) =
    new PostStringMessage(path, pattern format (args: _*))
}

object GetMessageFactory {
  def apply(path: String, keys: List[String])(args: String*) =
    new GetMessage(path, (keys zip args).toMap)
}*/


