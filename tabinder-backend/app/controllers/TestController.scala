package controllers

import cats.{MonadError, ~>}
import controllers.actions.Actions.ComposedActions
import javax.inject.Inject
import logger.MLogger
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import services.TabServiceAlgebra
import utils.Utils.AbstractGenericController
import utils.filter.GenericActionBuilder.GenericActionBuilder
import cats.syntax.all._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

trait TestControllerAlgebra {
  def someGet: Action[AnyContent]
}

class TestController[F[+ _]] @Inject()(cc: ControllerComponents,
                                       actions: ComposedActions[F],
                                       tabService: TabServiceAlgebra[F],
                                       val logger: MLogger[F])
                                      (implicit ec: ExecutionContext, toFuture: F ~> Future, M: MonadError[F, Throwable])
  extends AbstractGenericController(cc)
    with TestControllerAlgebra
    with BaseController[F] {

  override def someGet: Action[AnyContent] = actions.filtered.genericAsync {
    implicit request => tabService.getAll.map(tabs => Ok(Json.toJson(tabs)))
  }
}