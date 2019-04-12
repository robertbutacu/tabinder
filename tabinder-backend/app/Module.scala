import cats.effect.IO
import com.google.inject.{AbstractModule, TypeLiteral}
import concrete.Concrete._
import controllers.actions.Actions.{ComposedActions, ExtraRequest, RequestFiltered}
import controllers.{TabControllerAlgebra, TestControllerAlgebra}
import logger.MLogger
import repositories.TabRepositoryAlgebra
import services.TabServiceAlgebra

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(new TypeLiteral[TabServiceAlgebra[IO]] {}).to(classOf[IOTabService])
    bind(new TypeLiteral[TabRepositoryAlgebra[IO]] {}).to(classOf[IOTabRepository])
    bind(new TypeLiteral[TabControllerAlgebra] {}).to(classOf[IOTabController])
    bind(new TypeLiteral[MLogger[IO]] {}).to(classOf[IOPlayLogger])

    bind(new TypeLiteral[TestControllerAlgebra] {}).to(classOf[IOTestController])

    bind(new TypeLiteral[ComposedActions[IO]] {}).to(classOf[IOComposedActions])
    bind(new TypeLiteral[RequestFiltered[IO]] {}).to(classOf[IORequestFiltered])
    bind(new TypeLiteral[ExtraRequest[IO]] {}).to(classOf[IOExtraRequest])
  }
}