package models

import models.Types.{Artist, SongName, Tuning}
import play.api.libs.json.{Format, Json}
import models._

case class Tab(artist: Artist, songName: SongName, tab: Tuning)

object Tab {
  implicit val format: Format[Tab] = Json.format[Tab]
}
