package models.api

import play.api.libs.json.{Format, Json, OFormat}

case class HATEOASResource[A](value: A, selfLink: String)

object HATEOASResource {
  implicit def format[A](implicit aFormat: Format[A]): OFormat[HATEOASResource[A]] = Json.format[HATEOASResource[A]]
}
