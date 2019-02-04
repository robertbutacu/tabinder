package utils

import ai.snips.bsonmacros.DatabaseContext
import app.Routes
import controllers.{HomeController, TabController, TabControllerImpl}
import logger.{MLogger, PlayLogger}
import play.api.ApplicationLoader.Context
import play.api.mvc.EssentialFilter
import play.api.routing.Router
import cats.instances.future._
import play.api.BuiltInComponentsFromContext
import services.{TabService, TabServiceAlgebra}
import com.softwaremill.macwire.wire
import repositories.{TabRepository, TabRepositoryAlgebra}
import utils.FromFuture._
import scala.concurrent.{ExecutionContext, Future}

class AppLoader(context: Context) extends BuiltInComponentsFromContext(context) {
  lazy val ec: ExecutionContext = actorSystem.dispatcher

  lazy val prefix: String           = "/"

  lazy val homeController: HomeController        = wire[HomeController]

  lazy val dbContent: DatabaseContext            = wire[DatabaseContext]
  lazy val logger: MLogger[Future]               = wire[PlayLogger[Future]]

  lazy val repository: TabRepositoryAlgebra[Future] = wire[TabRepository[Future]]

  lazy val service: TabServiceAlgebra[Future]    = wire[TabService[Future]]

  lazy val tabControllerImpl: TabController      = wire[TabControllerImpl[Future]]

  lazy val appRouter: Routes = ???

  override lazy val router: Router = appRouter

  override def httpFilters: Seq[EssentialFilter] = Seq.empty[EssentialFilter]
}
