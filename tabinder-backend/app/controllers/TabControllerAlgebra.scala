package controllers

import cats.Monad
import cats.effect.IO
import cats.syntax.functor._
import javax.inject.Inject
import play.api.mvc._
import models.types.Types.{Artist, SongName, Tuning}
import models._
import play.api.libs.json.Json
import services.TabServiceAlgebra
import utils.FromFuture
import cats.instances.future._

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

trait TabControllerAlgebra {
  def post(): Action[AnyContent]
  def delete(): Action[AnyContent]

  def getByArtist(artist: Artist):  Action[AnyContent]
  def getByTuning(tuning: Tuning):  Action[AnyContent]
  def getBySong(songName: SongName): Action[AnyContent]
  def getAll(): Action[AnyContent]
}

class TabController[F[_]] @Inject()(tabService: TabServiceAlgebra[F], cc: ControllerComponents)
                                   (implicit ec: ExecutionContext, M: Monad[F], F: FromFuture[F])
  extends AbstractController(cc)
    with TabControllerAlgebra
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

  override def getAll(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      F.toFuture(tabService.getAll().map(tabs => Ok(Json.toJson(tabs))))
  }
}

class IOTabController @Inject()(val tabService: TabServiceAlgebra[IO],
                                val cc: ControllerComponents)(implicit ec: ExecutionContext) extends TabController[IO](tabService, cc)