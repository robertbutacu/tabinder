package controllers

import cats.Monad
import cats.syntax.functor._
import play.api.mvc._
import models.types.Types.{Artist, SongName, Tuning}
import models._
import play.api.libs.json.Json
import services.TabServiceAlgebra
import utils.FromFuture

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

trait TabControllerAlgebra {
  def post(): Action[AnyContent]
  def delete(): Action[AnyContent]

  def getByArtist(artist: Artist):  Action[AnyContent]
  def getByTuning(tuning: Tuning):  Action[AnyContent]
  def getBySong(songName: SongName): Action[AnyContent]
}

trait TabController extends TabControllerAlgebra

class TabControllerImpl[F[_]](tabService: TabServiceAlgebra[F], cc: ControllerComponents)
                             (implicit ec: ExecutionContext, M: Monad[F], F: FromFuture[F])
  extends AbstractController(cc)
    with TabController
    with BaseController {

  override def post(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      F.toFuture {
        withValidJson[Tab, F] {
          tab => tabService.post(tab).map(_ => Ok)
        }
      }
  }

  override def delete(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      F.toFuture {
        withValidJson[Tab, F] {
          tab => tabService.delete(tab).map(_ => Ok)
        }
      }
  }

  override def getByArtist(artist: Artist): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      F.toFuture {
        tabService.getByArtist(artist).map(tabs => Ok(Json.toJson(tabs)))
      }
  }

  override def getByTuning(tuning: Tuning): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      F.toFuture {
        tabService.getByTuning(tuning).map(tabs => Ok(Json.toJson(tabs)))
      }
  }

  override def getBySong(songName: SongName): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] => {
      F.toFuture {
        tabService.getBySong(songName).map(tabs => Ok(Json.toJson(tabs)))
      }
    }
  }
}