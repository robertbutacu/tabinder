package models.api

import play.api.libs.json._

case class HATEOASResource[A](fieldName: String, value: A, selfLink: String)

object HATEOASResource {
  implicit def writes[A](implicit aWrites: Writes[A]): Writes[HATEOASResource[A]] = new Writes[HATEOASResource[A]] {
    override def writes(o: HATEOASResource[A]): JsValue = {
      Json.obj(fields =
        o.fieldName -> aWrites.writes(o.value),
        "self"      -> JsString(o.selfLink)
      )
    }
  }
}
