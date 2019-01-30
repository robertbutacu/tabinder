package controllers

import logger.MLogger
import play.api.mvc._
import models.types.Types.{Artist, SongName, Tuning}
import models._
import services.TabServiceAlgebra

import scala.language.higherKinds

trait TabControllerAlgebra[F[_]] {
  def post(): Action[AnyContent]

  def delete(): Action[AnyContent]

  def getByArtist(artist: Artist): Action[AnyContent]

  def getByTuning(tuning: Tuning): Action[AnyContent]

  def getBySong(songName: SongName): Action[AnyContent]
}

class TabController[F[_]](tabService: TabServiceAlgebra[F],
                          cc: ControllerComponents) extends AbstractController(cc) with TabControllerAlgebra[F] {
  override def post(): Action[AnyContent] = Action.async {
      request =>
        ???
  }

  override def delete(): Action[AnyContent] = Action.async {
    request => ???
  }

  override def getByArtist(artist: Artist): Action[AnyContent] = Action.async {
    request => ???
  }

  override def getByTuning(tuning: Tuning): Action[AnyContent] = Action.async {
    request => ???
  }

  override def getBySong(songName: SongName): Action[AnyContent] = Action.async {
    request => ???
  }
}