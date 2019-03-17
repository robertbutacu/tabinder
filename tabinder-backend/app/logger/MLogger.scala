package logger

import cats.Applicative
import javax.inject.Inject
import play.api.Logger
import cats.syntax.applicative._

import scala.language.higherKinds

trait MLogger[F[_]] {
  def error(s: String):   F[Unit]
  def message(s: String): F[Unit]
  def warning(s: String): F[Unit]
}

class PlayLogger[F[_]: Applicative] @Inject() extends MLogger[F] {
  override def error(s: String): F[Unit]   = Logger.error(s"[Error] $s").pure
  override def message(s: String): F[Unit] = Logger.debug(s"[Message] $s").pure
  override def warning(s: String): F[Unit] = Logger.warn(s"[Warning] $s").pure
}
