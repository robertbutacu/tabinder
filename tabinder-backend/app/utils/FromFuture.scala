package utils

import cats.effect.IO
import cats.~>

import scala.concurrent.Future
import scala.language.higherKinds
object FromFuture {
  implicit def futureToIo: Future ~> IO = new ~>[Future, IO] {
    override def apply[A](fa: Future[A]): IO[A] = IO.fromFuture(IO(fa))
  }

  implicit def ioToFuture: IO ~> Future = new ~>[IO, Future] {
    override def apply[A](fa: IO[A]): Future[A] = fa.unsafeToFuture()
  }
}