package utils

import scala.concurrent.Future
import scala.language.higherKinds

trait FromFuture[F[_]] {
  def apply[R](f: => Future[R]): F[R]
}
