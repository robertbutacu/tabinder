import com.google.inject.AbstractModule
import play.api.ApplicationLoader.Context
import play.api.{Application, ApplicationLoader, LoggerConfigurator}
import utils.AppLoader

class Module extends AbstractModule with ApplicationLoader {
  def load(context: Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach { configurator =>
      configurator.configure(context.environment)
    }

    new AppLoader(context).application
  }

  override def configure(): Unit = {

  }
}