package logger

import cats.Monad
import cats.effect.IO
import javax.inject.Inject
import play.api.Logger

import scala.language.higherKinds

trait MLogger[F[_]] {
  def error(s: String):   F[Unit]
  def message(s: String): F[Unit]
  def warning(s: String): F[Unit]
}

class PlayLogger[F[_]] @Inject()(implicit M: Monad[F]) extends MLogger[F] {
  override def error(s: String): F[Unit]   = M.pure {
    Logger.error(s"[Error] $s")
  }

  override def message(s: String): F[Unit] = M.pure {
    Logger.debug(s"[Message] $s")
  }

  override def warning(s: String): F[Unit] = M.pure {
    Logger.warn(s"[Warning] $s")
  }
}
