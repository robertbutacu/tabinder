package concrete

import cats.effect.IO
import controllers.TabController
import javax.inject.Inject
import logger.MLogger
import play.api.mvc.ControllerComponents
import play.modules.reactivemongo.ReactiveMongoApi
import repositories.{TabRepository, TabRepositoryAlgebra}
import services.{TabService, TabServiceAlgebra}
import cats.instances.all._
import utils.FromFuture._
import scala.concurrent.ExecutionContext

object Concrete {

  class IOTabController @Inject()(val tabService: TabServiceAlgebra[IO],
                                  val cc: ControllerComponents,
                                  override val logger: MLogger[IO])(implicit ec: ExecutionContext) extends TabController[IO](tabService, cc, logger)


  class IOTabRepository @Inject()(implicit reactiveMongoApi: ReactiveMongoApi,
                                  ec: ExecutionContext) extends TabRepository[IO]


  class IOTabService @Inject()(tabRepository: TabRepositoryAlgebra[IO],
                               logger: MLogger[IO]) extends TabService[IO](tabRepository, logger)

}
