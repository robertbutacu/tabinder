package utils

import play.api.mvc.{AnyContent, Request}

import scala.concurrent.{ExecutionContext, Future}

trait EffectSupport {
  implicit val futureFromFuture: FromFuture[Future] = new FromFuture[Future] {
    override def fromFuture[R](f: => Future[R])(implicit ec: ExecutionContext): Future[R] = f

    override def toFuture[R](f: => Future[R])(implicit request: Request[AnyContent], ec: ExecutionContext): Future[R] = f
  }
}
