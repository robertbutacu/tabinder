package models

import models.types.Types.{Artist, SongName, Tuning}
import models.types._
import play.api.libs.json.{Json, OFormat}
import eu.timepit.refined.auto._

case class Tab(artist: Artist,
               songName: SongName,
               tuning: Tuning,
               notes: Option[String] = None)

object Tab {
  implicit val format: OFormat[Tab] = Json.format[Tab]
}
