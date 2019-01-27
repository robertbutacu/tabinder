package services

import models.Tab
import models.Types.{Artist, SongName, Tuning}

import scala.language.higherKinds

trait TabService[F[_]] {
  def post(tab: Tab): F[Unit]
  def delete(tab: Tab): F[Unit]
  def getByArtist(artist: Artist):   F[List[Tab]]
  def getByTuning(tuning: Tuning):   F[List[Tab]]
  def getBySong(songName: SongName): F[List[Tab]]
}
