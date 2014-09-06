package hu.gansperger.neptunapi

import com.github.nscala_time.time.Imports._

case class NeptunMailPreview(id: String, read: Boolean, subject: String, sender: String, date: DateTime)
case class NeptunMail(subject: String, sender: String, sendDate: DateTime, dueDate: Option[DateTime], receivers: String, message: String)