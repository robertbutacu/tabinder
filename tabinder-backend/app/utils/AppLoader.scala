package utils

import ai.snips.bsonmacros.DatabaseContext
import app.Routes
import controllers.{HomeController, TabControllerImpl}
import logger.{MLogger, PlayLogger}
import play.api.ApplicationLoader.Context
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import cats.instances.future._
import play.api.BuiltInComponentsFromContext
import services.TabService
import com.softwaremill.macwire.wire
import repositories.TabRepository

import scala.concurrent.{ExecutionContext, Future}

class AppLoader(context: Context) extends BuiltInComponentsFromContext(context){
  lazy val ec: ExecutionContext = actorSystem.dispatcher

  lazy val prefix: String           = "/"
  lazy val appRouter: Routes = wire[Routes]

  lazy val tabController: TabControllerImpl[Future] = wire[TabControllerImpl[Future]]
  lazy val homeController: HomeController           = wire[HomeController]
  lazy val service: TabService[Future]           = wire[TabService[Future]]
  lazy val repository: TabRepository[Future]     = wire[TabRepository[Future]]
  lazy val logger: MLogger[Future]               = wire[PlayLogger[Future]]
  lazy val dbContent: DatabaseContext            = wire[DatabaseContext]

  override lazy val router: Router = appRouter

  override def httpFilters: Seq[EssentialFilter] = Seq.empty[EssentialFilter]
}
