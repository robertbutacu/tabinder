package models

import models.Types.{Artist, SongName, Tuning}
import play.api.libs.json.{Format, Json}
import models._
import org.mongodb.scala.bson.collection.immutable.Document
import eu.timepit.refined.auto._
import org.mongodb.scala.bson.BsonString

case class Tab(artist: Artist, songName: SongName, tuning: Tuning)

object Tab {
  implicit val format: Format[Tab] = Json.format[Tab]

  def toDocument(tab: Tab): Document = Document(
    "artist"   -> BsonString(tab.artist),
    "songName" -> BsonString(tab.songName),
    "tuning"   -> BsonString(tab.songName)
  )
}