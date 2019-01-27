package repositories

import models.Tab

import scala.language.higherKinds

trait TabRepository[F[_]] {
  def add(tab: Tab): F[Unit]
  def getByArtist(): F[List[Tab]]
  def getByTuning(): F[List[Tab]]
  def getBySong():   F[List[Tab]]
}
