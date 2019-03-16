import cats.effect.IO
import com.google.inject.{AbstractModule, TypeLiteral}
import concrete.Concrete._
import controllers.TabControllerAlgebra
import logger.{IOPlayLogger, MLogger}
import repositories.TabRepositoryAlgebra
import services.TabServiceAlgebra

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(new TypeLiteral[TabServiceAlgebra[IO]] {}).to(classOf[IOTabService])
    bind(new TypeLiteral[TabRepositoryAlgebra[IO]] {}).to(classOf[IOTabRepository])
    bind(new TypeLiteral[TabControllerAlgebra] {}).to(classOf[IOTabController])
    bind(new TypeLiteral[MLogger[IO]] {}).to(classOf[IOPlayLogger])
  }
}