package hu.gansperger.neptunapi

import com.github.nscala_time.time.Imports._

sealed trait NeptunState
trait BaseState extends NeptunState
trait LoggedInState extends NeptunState
trait MainCalledState extends LoggedInState
trait LoginableState extends BaseState

//case class NeptunState(loginTime: Option[DateTime], mainCalled: Boolean)

