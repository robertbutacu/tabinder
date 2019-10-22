package services

import cats.Monad
import cats.syntax.all._
import controllers.routes
import javax.inject.Inject
import logger.MLogger
import models.api.HATEOASResource
import models.data.Tab
import models.types.Types.{Artist, SongName, Tuning}
import repositories.TabRepositoryAlgebra

import scala.language.higherKinds

trait TabServiceAlgebra[F[_]] {
  def post(tab: Tab):   F[Unit]
  def delete(tab: Tab): F[Unit]

  def getAllArtists:               F[Set[HATEOASResource[Artist]]]
  def getByArtist(artist: Artist): F[List[Tab]]

  def getAllTunings:               F[Set[HATEOASResource[Tuning]]]
  def getByTuning(tuning: Tuning): F[List[Tab]]

  def getAllSongs:                   F[Set[HATEOASResource[SongName]]]
  def getBySong(songName: SongName): F[List[Tab]]

  def getAll:                      F[List[Tab]]
}

class TabService[F[_]: Monad] @Inject()(tabRepository: TabRepositoryAlgebra[F],
                                        logger: MLogger[F]) extends TabServiceAlgebra[F] {

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

  override def getAll: F[List[Tab]] = {
    for {
      _    <- logger.message("Retrieving all songs")
      tabs <- tabRepository.getAll
    } yield tabs
  }

  override def getAllArtists: F[Set[HATEOASResource[Artist]]] = {
    for {
      _       <- logger.message("Retrieving all artists")
      artists <- tabRepository.getAllArtists
      result  = artists.map(a => HATEOASResource("artist", a, routes.TabControllerAlgebra.getByArtist(a).url))
    } yield result
  }

  override def getAllTunings: F[Set[HATEOASResource[Tuning]]] = {
    for {
      _       <- logger.message("Retrieving all tunings")
      tunings <- tabRepository.getAllTunings
      result  = tunings.map(t => HATEOASResource("tuning", t, routes.TabControllerAlgebra.getByTuning(t).url))
    } yield result
  }

  override def getAllSongs: F[Set[HATEOASResource[SongName]]] = {
    for {
      _     <- logger.message("Retrieving all songs")
      songs <- tabRepository.getAllSongs
      result  = songs.map(s => HATEOASResource("songname", s, routes.TabControllerAlgebra.getBySong(s).url))
    } yield result
  }
}
