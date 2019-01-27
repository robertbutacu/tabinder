package controllers

import models.Types.{Artist, SongName, Tuning}
import play.api.mvc.{Action, AnyContent}
import models._

import scala.language.higherKinds

trait TabController[F[_]] {
  def post(): Action[AnyContent]
  def delete(): Action[AnyContent]
  def getByArtist(artist: Artist):   Action[AnyContent]
  def getByTuning(tuning: Tuning):   Action[AnyContent]
  def getBySong(songName: SongName): Action[AnyContent]
}
