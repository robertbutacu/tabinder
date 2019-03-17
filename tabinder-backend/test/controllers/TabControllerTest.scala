package controllers

import models.types.Types.{Artist, SongName, Tuning}
import play.api.mvc.{Action, AnyContent}

class TabControllerTest extends TabControllerAlgebra {
  override def post(): Action[AnyContent] = ???

  override def delete(): Action[AnyContent] = ???

  override def getByArtist(artist: Artist): Action[AnyContent] = ???

  override def getByTuning(tuning: Tuning): Action[AnyContent] = ???

  override def getBySong(songName: SongName): Action[AnyContent] = ???

  override def getAll(): Action[AnyContent] = ???
}
