package repositories

import models.Tab
import models.Types.{Artist, SongName, Tuning}

import scala.language.higherKinds

trait TabRepository[F[_]] {
  def create(tab: Tab): F[Unit]
  def remove(tab: Tab): F[Unit]
  def getByArtist(artist: Artist):   F[List[Tab]]
  def getByTuning(tuning: Tuning):   F[List[Tab]]
  def getBySong(songName: SongName): F[List[Tab]]
}
