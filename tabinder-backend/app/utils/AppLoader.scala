package utils

import ai.snips.bsonmacros.DatabaseContext
import app.Routes
import cats.Monad
import cats.effect.IO
import controllers.{TabControllerAlgebra, TabController}
import logger.{MLogger, PlayLogger}
import play.api.ApplicationLoader.Context
import play.api.mvc._
import play.api.routing.Router
import cats.instances.future._
import play.api.{BuiltInComponents, BuiltInComponentsFromContext}
import services.{TabService, TabServiceAlgebra}
import com.softwaremill.macwire.wire
import play.api.http._
import repositories.{TabRepository, TabRepositoryAlgebra}

import scala.concurrent.{ExecutionContext, Future}


/*
class AppLoader(context: Context) extends BuiltInComponentsFromContext(context)
  with EffectSupport {

  implicit val ec: ExecutionContext = actorSystem.dispatcher

  lazy val prefix: String           = "/"
  lazy val cc: ControllerComponents                 = wire[DefaultControllerComponents]
  lazy val dbContent: DatabaseContext               = wire[DatabaseContext]
  lazy val logger: MLogger[Future]                  = wire[PlayLogger[Future]]

  lazy val repository: TabRepositoryAlgebra[Future] = wire[TabRepository[Future]]

  lazy val service: TabServiceAlgebra[Future]       = wire[TabService[Future]]

  lazy val tabControllerImpl: TabController         = wire[TabControllerImpl[Future]]

  val appRouter: Routes       = new Routes(httpErrorHandler, tabControllerImpl, prefix)

  override val router: Router = appRouter

  override def httpFilters: Seq[EssentialFilter] = Seq.empty[EssentialFilter]
}
*/
