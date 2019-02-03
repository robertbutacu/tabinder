package utils

import cats.effect.IO
import play.api.mvc.{AnyContent, Request}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

trait FromFuture[F[_]] {
  def fromFuture[R](f: => Future[R])(implicit ec: ExecutionContext): F[R]
  def toFuture[R](f: => F[R])(implicit request: Request[AnyContent], ec: ExecutionContext): Future[R]
}

object FromFuture {
  implicit val futureFromFuture: FromFuture[Future] = new FromFuture[Future] {
    override def fromFuture[R](f: => Future[R])(implicit ec: ExecutionContext): Future[R] = f

    override def toFuture[R](f: => Future[R])(implicit request: Request[AnyContent], ec: ExecutionContext): Future[R] = f
  }


  // helpful for testing maybe???
  implicit val tryFromFuture: FromFuture[Try] = new FromFuture[Try] {
    override def fromFuture[R](f: => Future[R])(implicit ec: ExecutionContext): Try[R] = {
      Try(Await.result(f, Duration.Inf))
    }

    override def toFuture[R](f: => Try[R])(implicit request: Request[AnyContent], ec: ExecutionContext): Future[R] = {
      f match {
        case Success(r)   => Future.successful(r)
        case Failure(err) => Future.failed(err)
      }
    }
  }

  implicit val ioFromFuture: FromFuture[IO] = new FromFuture[IO] {
    override def fromFuture[R](f: => Future[R])(implicit ec: ExecutionContext): IO[R] = {
      IO.fromFuture(IO(f))
    }

    override def toFuture[R](f: => IO[R])(implicit request: Request[AnyContent], ec: ExecutionContext): Future[R] = {
      f.unsafeToFuture()
    }
  }
}