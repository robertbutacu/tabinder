package repositories

import cats.MonadError
import cats.effect.IO
import javax.inject.Inject
import models.Tab
import models.types.Types.{Artist, SongName, Tuning}
import play.api.libs.json.{JsString, Json}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.Cursor.FailOnError
import reactivemongo.api.ReadPreference
import reactivemongo.play.json.collection.JSONCollection
import utils.FromFuture
import cats.syntax.flatMap._
import reactivemongo.play.json.ImplicitBSONHandlers
import scala.concurrent.ExecutionContext
import scala.language.higherKinds


trait TabRepositoryAlgebra[F[_]] {
  def create(tab: Tab): F[Unit]
  def remove(tab: Tab): F[Unit]
  def getByArtist(artist: Artist):   F[List[Tab]]
  def getByTuning(tuning: Tuning):   F[List[Tab]]
  def getBySong(songName: SongName): F[List[Tab]]
  def getAll():                      F[List[Tab]]
}

class TabRepository[F[_]] @Inject()()(implicit M: MonadError[F, Throwable],
                                      F: FromFuture[F],
                                      reactiveMongoApi: ReactiveMongoApi,
                                      ec: ExecutionContext) extends TabRepositoryAlgebra[F] {

  def collection: F[JSONCollection] = F.fromFuture {
    reactiveMongoApi.database.map(_.collection("tabinder"))
  }

  override def create(tab: Tab): F[Unit] = M.pure {
    collection.flatMap(c => F.fromFuture(c.insert(tab)))
  }

  override def remove(tab: Tab): F[Unit] = M.pure {
    collection.flatMap(c => F.fromFuture{ c.findAndRemove(tab).map(_ => ()) } )
  }

  override def getByArtist(artist: Artist): F[List[Tab]] = findAll("artist", artist.value)

  override def getByTuning(tuning: Tuning): F[List[Tab]] = findAll("tuning", tuning.value)

  override def getBySong(songName: SongName): F[List[Tab]] = findAll("songName", songName)

  override def getAll(): F[List[Tab]] = findAll("", "")

  private def findAll(field: String, value: String): F[List[Tab]] = {
    collection.flatMap(c => F.fromFuture {
      c.find(Json.obj(field -> JsString(value)))(implicitly[ImplicitBSONHandlers.JsObjectDocumentWriter.type])
        .cursor[Tab](ReadPreference.primaryPreferred)
        .collect(Int.MaxValue, FailOnError[List[Tab]]())
    })
  }
}

class IOTabRepository @Inject()(implicit reactiveMongoApi: ReactiveMongoApi,
                                ec: ExecutionContext) extends TabRepository[IO]