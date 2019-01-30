package utils

import play.api.mvc.{AnyContent, Request}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

trait FromFuture[F[_]] {
  def fromFuture[R](f: => Future[R]): F[R]
  def toFuture[R](f: => F[R])(implicit request: Request[AnyContent], ec: ExecutionContext): Future[R]
}

object FromFuture {
  implicit val futureFromFuture: FromFuture[Future] = new FromFuture[Future] {
    override def fromFuture[R](f: => Future[R]): Future[R] = f

    override def toFuture[R](f: => Future[R])(implicit request: Request[AnyContent], ec: ExecutionContext): Future[R] = f
  }
}