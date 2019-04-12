package utils.filter

import cats.{Functor, ~>}
import play.api.mvc.{ActionFilter, Result}

import scala.language.higherKinds
import cats.syntax.all._

import scala.concurrent.Future

trait GenericActionFilter[F[_], R[_]] extends ActionFilter[R] {
  implicit def transformer: F ~> Future

  def genericFilter[A](request: R[A]): F[Option[Result]]

  override protected def filter[A](request: R[A]): Future[Option[Result]] = transformer(genericFilter(request))
}
