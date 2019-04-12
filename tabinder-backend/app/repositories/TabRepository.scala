package repositories

import cats.syntax.all._
import cats.~>
import javax.inject.Inject
import models.data.Tab
import models.types.Types.{Artist, SongName, ThrowableMonadError, Tuning}
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.Cursor.FailOnError
import reactivemongo.api.ReadPreference
import reactivemongo.play.json.ImplicitBSONHandlers
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

trait TabRepositoryAlgebra[F[_]] {
  def create(tab: Tab): F[Unit]
  def remove(tab: Tab): F[Unit]

  def getAllArtists:               F[Set[Artist]]
  def getByArtist(artist: Artist):   F[List[Tab]]

  def getAllTunings:               F[Set[Tuning]]
  def getByTuning(tuning: Tuning):   F[List[Tab]]

  def getAllSongs:                 F[Set[SongName]]
  def getBySong(songName: SongName): F[List[Tab]]

  def getAll:                      F[List[Tab]]
}

class TabRepository[F[_]: ThrowableMonadError] @Inject()()(implicit fromFuture: Future ~> F,
                                                           reactiveMongoApi: ReactiveMongoApi,
                                                           ec: ExecutionContext) extends TabRepositoryAlgebra[F] {

  def collection: F[JSONCollection] = fromFuture {
    reactiveMongoApi.database.map(_.collection("tabinder"))
  }

  override def create(tab: Tab): F[Unit] = collection.flatMap(c => fromFuture(c.insert(tab).map(_ => ())))
  override def remove(tab: Tab): F[Unit] = collection.flatMap(c => fromFuture{ c.findAndRemove(tab).map(_ => ()) } )

  override def getByArtist(artist: Artist): F[List[Tab]]   = findAll(Json.obj("artist" -> artist.value))
  override def getByTuning(tuning: Tuning): F[List[Tab]]   = findAll(Json.obj("tuning" -> tuning.value))
  override def getBySong(songName: SongName): F[List[Tab]] = findAll(Json.obj("songName" -> songName))
  override def getAll: F[List[Tab]]                      = findAll(JsObject.empty)

  private def findAll(jsObject: JsObject): F[List[Tab]] = {
    collection.flatMap(c => fromFuture {
      c.find(jsObject)(implicitly[ImplicitBSONHandlers.JsObjectDocumentWriter.type])
        .cursor[Tab](ReadPreference.primaryPreferred)
        .collect(Int.MaxValue, FailOnError[List[Tab]]())
    })
  }

  override def getAllArtists: F[Set[Artist]] = {
    findAll(JsObject.empty).map(_.map(_.artist).toSet)
  }

  override def getAllTunings: F[Set[Tuning]] = {
    findAll(JsObject.empty).map(_.map(_.tuning).toSet)
  }

  override def getAllSongs: F[Set[SongName]] = {
    findAll(JsObject.empty).map(_.map(_.songName).toSet)
  }
}
