package hu.gansperger

import hu.gansperger.neptunapi.data.{NeptunMailPreview, NeptunMail}

package object neptunapi {
  def ignore[T](a : T) = ()

  type MessageListType = Seq[NeptunMailPreview]
  type MessageType = NeptunMail

  class NeptunException(reason: String) extends Exception(reason) { }
  class LoginException(reason: String) extends NeptunException(reason) { }
}
