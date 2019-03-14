package repositories

import ai.snips.bsonmacros.{BaseDAO, DatabaseContext}
import cats.MonadError
import cats.effect.IO
import eu.timepit.refined.auto._
import javax.inject.Inject
import models.Tab
import models.types.Types.{Artist, SongName, Tuning}
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.BsonString
import org.mongodb.scala.bson.collection.immutable.Document
import utils.FromFuture

import scala.concurrent.ExecutionContext
import scala.language.higherKinds
import scala.reflect.ClassTag

trait TabRepositoryAlgebra[F[_]] {
  def create(tab: Tab): F[Unit]
  def remove(tab: Tab): F[Unit]
  def getByArtist(artist: Artist):   F[List[Tab]]
  def getByTuning(tuning: Tuning):   F[List[Tab]]
  def getBySong(songName: SongName): F[List[Tab]]
  def getAll():                      F[List[Tab]]
}

class TabRepository[F[_]] @Inject()(dbContext: DatabaseContext)(implicit M: MonadError[F, Throwable],
                                                      F: FromFuture[F],
                                                      ec: ExecutionContext) extends BaseDAO[Tab] with TabRepositoryAlgebra[F] {
  val db = dbContext.database("sample_db")

  override val collection: MongoCollection[Tab] = db.getCollection[Tab]("tabs")

  override def create(tab: Tab): F[Unit] = M.pure {
    collection.insertOne(tab)
  }

  override def remove(tab: Tab): F[Unit] = M.pure {
    collection.deleteOne(Tab.toDocument(tab))
  }

  override def getByArtist(artist: Artist): F[List[Tab]] = findAll("artist", BsonString(artist))

  override def getByTuning(tuning: Tuning): F[List[Tab]] = findAll("tuning", BsonString(tuning))

  override def getBySong(songName: SongName): F[List[Tab]] = findAll("songName", BsonString(songName))

  private def findAll[T](field: String, value: BsonString)(implicit ct: ClassTag[T]): F[List[T]] = F.fromFuture {
    collection.find[T](Document(field -> value)).toFuture().map(_.toList)
  }

  override def getAll(): F[List[Tab]] = F.fromFuture{ collection.find().toFuture().map(_.toList) }
}

class IOTabRepository @Inject()(dbContext: DatabaseContext)(implicit ec: ExecutionContext) extends TabRepository[IO](dbContext)