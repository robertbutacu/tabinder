package concrete

import cats.effect.IO
import controllers.{TabController, TestController}
import javax.inject.Inject
import logger.{MLogger, PlayLogger}
import play.api.mvc.ControllerComponents
import play.modules.reactivemongo.ReactiveMongoApi
import repositories.{TabRepository, TabRepositoryAlgebra}
import services.{TabService, TabServiceAlgebra}
import cats.instances.all._
import controllers.actions.Actions.{ComposedActions, ExtraRequest, RequestFiltered}
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

  class IOPlayLogger extends PlayLogger[IO]

  class IOTestController @Inject()(cc: ControllerComponents,
                                   actions: ComposedActions[IO],
                                   override val logger: MLogger[IO])(implicit ec: ExecutionContext) extends TestController[IO](cc, actions, logger)

  class IOComposedActions @Inject()(
                                       requestFiltered: RequestFiltered[IO],
                                       extraRequest: ExtraRequest[IO],
                                       cc: ControllerComponents
                                     )(implicit ec: ExecutionContext) extends ComposedActions[IO](requestFiltered, extraRequest, cc)

  class IORequestFiltered @Inject()(implicit ec: ExecutionContext) extends RequestFiltered[IO]
  class IOExtraRequest @Inject()(implicit ec: ExecutionContext)    extends ExtraRequest[IO]
}
