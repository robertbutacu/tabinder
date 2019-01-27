package controllers

import models.Types.{Artist, SongName, Tuning}
import play.api.mvc.{Action, AnyContent}
import models._

import scala.language.higherKinds

trait TabControllerAlgebra[F[_]] {
  def post(): Action[AnyContent]
  def delete(): Action[AnyContent]
  def getByArtist(artist: Artist):   Action[AnyContent]
  def getByTuning(tuning: Tuning):   Action[AnyContent]
  def getBySong(songName: SongName): Action[AnyContent]
}

class TabController[F[_]] extends TabControllerAlgebra[F] {
  override def post(): Action[AnyContent] = ???

  override def delete(): Action[AnyContent] = ???

  override def getByArtist(artist: Artist): Action[AnyContent] = ???

  override def getByTuning(tuning: Tuning): Action[AnyContent] = ???

  override def getBySong(songName: SongName): Action[AnyContent] = ???
}