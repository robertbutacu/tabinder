package services

import cats.Monad
import cats.effect.IO
import javax.inject.Inject
import logger.MLogger
import models.Tab
import models.types.Types.{Artist, SongName, Tuning}
import repositories.TabRepositoryAlgebra
import cats.syntax.flatMap._
import cats.syntax.functor._
import scala.language.higherKinds

trait TabServiceAlgebra[F[_]] {
  def post(tab: Tab): F[Unit]
  def delete(tab: Tab): F[Unit]
  def getByArtist(artist: Artist):   F[List[Tab]]
  def getByTuning(tuning: Tuning):   F[List[Tab]]
  def getBySong(songName: SongName): F[List[Tab]]
  def getAll():                      F[List[Tab]]
}

class TabService[F[_]: Monad] @Inject()(tabRepository: TabRepositoryAlgebra[F], logger: MLogger[F]) extends TabServiceAlgebra[F] {
  override def post(tab: Tab): F[Unit] = {
    for {
      _ <- logger.message("Creating tab: " + tab)
      _ <- tabRepository.create(tab)
    } yield ()
  }

  override def delete(tab: Tab): F[Unit] = {
    for {
      _ <- logger.message("Deleting tab: " + tab)
      _ <- tabRepository.remove(tab)
    } yield ()
  }

  override def getByArtist(artist: Artist): F[List[Tab]] = {
    for {
      _    <- logger.message("Retrieving all tabs by artist:" + artist)
      tabs <- tabRepository.getByArtist(artist)
    } yield tabs
  }

  override def getByTuning(tuning: Tuning): F[List[Tab]] = {
    for {
      _    <- logger.message("Retrieving all tabs by tuning:" + tuning)
      tabs <- tabRepository.getByTuning(tuning)
    } yield tabs
  }

  override def getBySong(songName: SongName): F[List[Tab]] = {
    for {
      _    <- logger.message("Retrieving all tabs by song:" + songName)
      tabs <- tabRepository.getBySong(songName)
    } yield tabs
  }

  override def getAll(): F[List[Tab]] = {
    for {
      _    <- logger.message("Retrieving all songs")
      tabs <- tabRepository.getAll()
    } yield tabs
  }
}

class IOTabService @Inject()(tabRepository: TabRepositoryAlgebra[IO],
                             logger: MLogger[IO]) extends TabService[IO](tabRepository, logger)