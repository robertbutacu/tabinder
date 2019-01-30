package models

import eu.timepit.refined._
import eu.timepit.refined.api._
import play.api.libs.functional.syntax._
import play.api.libs.json._

package object types {
  implicit def refinedReads[T, P](implicit tReads: Reads[T], validate: Validate[T, P]): Reads[Refined[T, P]] = (json: JsValue) => {
    tReads.reads(json).flatMap { t =>
      refineV[P](t) match {
        case Left(err) => JsError(err)
        case Right(value) => JsSuccess(value)
      }
    }
  }

  implicit def refinedWrites[T, P](implicit writes: Writes[T]): Writes[Refined[T, P]] = writes.contramap(refined => refined.value)

}
