package utils

import scala.concurrent.Future
import scala.language.higherKinds

trait FromFuture[F[_]] {
  def apply[R](f: => Future[R]): F[R]
}

object FromFuture {
  implicit val futureFromFuture: FromFuture[Future] = new FromFuture[Future] {
    override def apply[R](f: => Future[R]): Future[R] = f
  }
}