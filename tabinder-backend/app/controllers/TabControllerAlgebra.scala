package controllers

import cats.syntax.functor._
import cats.{MonadError, ~>}
import javax.inject.Inject
import logger.MLogger
import models._
import models.types.Types.{Artist, SongName, Tuning}
import play.api.libs.json.Json
import play.api.mvc._
import services.TabServiceAlgebra

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

class TabController[F[_]] @Inject()(tabService: TabServiceAlgebra[F],
                                    cc: ControllerComponents,
                                    val logger: MLogger[F])
                                   (implicit ec: ExecutionContext, toFuture: F ~> Future, M: MonadError[F, Throwable])
  extends AbstractController(cc)
    with TabControllerAlgebra
    with BaseController[F] {
  override def post(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      toFuture {
        withRecover {
          withValidJson[Tab] {
            tab => tabService.post(tab).map(_ => Ok)
          }
        }
      }
  }

  override def delete(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      toFuture {
        withRecover {
          withValidJson[Tab] {
            tab => tabService.delete(tab).map(_ => Ok)
          }
        }
      }
  }

  override def getByArtist(artist: Artist): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      toFuture {
        withRecover {
          tabService.getByArtist(artist).map(tabs => Ok(Json.toJson(tabs)))
        }
      }
  }

  override def getByTuning(tuning: Tuning): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      toFuture {
        withRecover {
          tabService.getByTuning(tuning).map(tabs => Ok(Json.toJson(tabs)))
        }
      }
  }

  override def getBySong(songName: SongName): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] => {
      toFuture {
        withRecover {
          tabService.getBySong(songName).map(tabs => Ok(Json.toJson(tabs)))
        }
      }
    }
  }

  override def getAll(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      toFuture {
        withRecover {
          tabService.getAll().map(tabs => Ok(Json.toJson(tabs)))
        }
      }
  }
}