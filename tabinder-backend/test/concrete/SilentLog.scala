package concrete

import cats.Applicative
import logger.MLogger
import cats.syntax.all._

class SilentLog[F[_]: Applicative]() extends MLogger[F]{
  override def error(s: String): F[Unit] = ().pure

  override def message(s: String): F[Unit] = ().pure

  override def warning(s: String): F[Unit] = ().pure
}
