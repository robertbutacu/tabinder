package repositories

import cats.MonadError
import models.Tab
import models.Types.{Artist, SongName, Tuning}
import models._
import org.bson.conversions.Bson
import org.mongodb.scala.bson.BsonDocument
import org.mongodb.scala.model.Filters._

import scala.language.higherKinds

trait TabRepositoryAlgebra[F[_]] {
  def create(tab: Tab): F[Unit]
  def remove(tab: Tab): F[Unit]
  def getByArtist(artist: Artist):   F[List[Tab]]
  def getByTuning(tuning: Tuning):   F[List[Tab]]
  def getBySong(songName: SongName): F[List[Tab]]
}

class TabRepository[F[_]](implicit M: MonadError[F, Throwable]) extends  MongoRepository[Tab]("", "tabs") with TabRepositoryAlgebra[F] {

  override def create(tab: Tab): F[Unit] = M.pure {
    collection.insertOne(tab)
  }

  override def remove(tab: Tab): F[Unit] = M.pure {
    //BsonDocument().replace("asdf", tab.artist)
    collection.deleteOne(equal("artist", tab.artist))
  }

  override def getByArtist(artist: Artist): F[List[Tab]] = {???}

  override def getByTuning(tuning: Tuning): F[List[Tab]] = ???

  override def getBySong(songName: SongName): F[List[Tab]] = ???
}