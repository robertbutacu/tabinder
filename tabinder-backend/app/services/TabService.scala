package services

import models.Tab
import models.Types.{Artist, SongName, Tuning}
import repositories.TabRepositoryAlgebra

import scala.language.higherKinds

trait TabServiceAlgebra[F[_]] {
  def post(tab: Tab): F[Unit]
  def delete(tab: Tab): F[Unit]
  def getByArtist(artist: Artist):   F[List[Tab]]
  def getByTuning(tuning: Tuning):   F[List[Tab]]
  def getBySong(songName: SongName): F[List[Tab]]
}

class TabService[F[_]](tabRepository: TabRepositoryAlgebra[F]) extends TabServiceAlgebra[F] {
  override def post(tab: Tab): F[Unit] = tabRepository.create(tab)

  override def delete(tab: Tab): F[Unit] = tabRepository.remove(tab)

  override def getByArtist(artist: Artist): F[List[Tab]] = tabRepository.getByArtist(artist)

  override def getByTuning(tuning: Tuning): F[List[Tab]] = tabRepository.getByTuning(tuning)

  override def getBySong(songName: SongName): F[List[Tab]] = tabRepository.getBySong(songName)
}