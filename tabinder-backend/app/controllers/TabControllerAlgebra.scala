package controllers

import cats.syntax.functor._
import cats.{MonadError, ~>}
import javax.inject.Inject
import logger.MLogger
import models.types._
import models.types.Types.{Artist, SongName, Tuning}
import play.api.libs.json.Json
import play.api.mvc._
import services.TabServiceAlgebra
import utils.Utils.AbstractGenericController
import models.data.Tab
import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

trait TabControllerAlgebra {
  def post():   Action[AnyContent]
  def delete(): Action[AnyContent]

  def getAllArtists:               Action[AnyContent]
  def getByArtist(artist: Artist): Action[AnyContent]

  def getAllTunings:               Action[AnyContent]
  def getByTuning(tuning: Tuning): Action[AnyContent]

  def getAllSongs:                   Action[AnyContent]
  def getBySong(songName: SongName): Action[AnyContent]

  def getAll:                      Action[AnyContent]
}

class TabController[F[_]] @Inject()(tabService: TabServiceAlgebra[F],
                                    cc: ControllerComponents,
                                    val logger: MLogger[F])
                                   (implicit ec: ExecutionContext, toFuture: F ~> Future, M: MonadError[F, Throwable])
  extends AbstractGenericController(cc)
    with TabControllerAlgebra
    with BaseController[F] {
  override def post(): Action[AnyContent] = GenericAction.async(parse.anyContent) {
    implicit request =>
      withRecover {
        withValidJson[Tab] {
          tab => tabService.post(tab).map(_ => Ok)
        }
      }
  }

  override def delete(): Action[AnyContent] = GenericAction.async(parse.anyContent) {
    implicit request: Request[AnyContent] =>
      withRecover {
        withValidJson[Tab] {
          tab => tabService.delete(tab).map(_ => Ok)
        }
      }
  }

  override def getByArtist(artist: Artist): Action[AnyContent] = GenericAction.async {
    implicit request: Request[AnyContent] =>
      withRecover {
        tabService.getByArtist(artist).map(tabs => Ok(Json.toJson(tabs)))
      }
  }

  override def getByTuning(tuning: Tuning): Action[AnyContent] = GenericAction.async {
    implicit request: Request[AnyContent] =>
      withRecover {
        tabService.getByTuning(tuning).map(tabs => Ok(Json.toJson(tabs)))
      }
  }

  override def getBySong(songName: SongName): Action[AnyContent] = GenericAction.async {
    implicit request: Request[AnyContent] => {
      withRecover {
        tabService.getBySong(songName).map(tabs => Ok(Json.toJson(tabs)))
      }
    }
  }

  override def getAll: Action[AnyContent] = GenericAction.async {
    implicit request: Request[AnyContent] =>
      withRecover {
        tabService.getAll.map(tabs => Ok(Json.toJson(tabs)))
      }
  }

  override def getAllArtists: Action[AnyContent] = GenericAction.async {
    implicit request: Request[AnyContent] =>
      withRecover {
        tabService.getAllArtists.map(artists => Ok(Json.toJson(artists)))
      }
  }

  override def getAllTunings: Action[AnyContent] = GenericAction.async {
    implicit request: Request[AnyContent] =>
      withRecover {
        tabService.getAllTunings.map(tunings => Ok(Json.toJson(tunings)))
      }
  }

  override def getAllSongs: Action[AnyContent] = GenericAction.async {
    implicit request: Request[AnyContent] =>
      withRecover {
        tabService.getAllSongs.map(songs => Ok(Json.toJson(songs)))
      }
  }
}