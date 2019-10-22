package models.data

import cats.Eq
import models.types.Types.{Artist, SongName, Tuning}
import play.api.libs.json.{Json, OFormat}
import models.types._

case class Tab(artist:   Artist,
               songName: SongName,
               tuning:   Tuning,
               notes:    Option[String] = None)

object Tab {
  implicit val eqInstance: Eq[Tab] = new Eq[Tab] {
    override def eqv(x: Tab, y: Tab): Boolean = x == y
  }

  implicit val format: OFormat[Tab] = Json.format[Tab]
}
