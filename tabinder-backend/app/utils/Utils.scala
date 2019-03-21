package utils


import cats.~>
import play.api.mvc.{Action, ActionBuilder, BodyParser, Result}

import scala.concurrent.Future
import scala.language.higherKinds

object Utils {
  implicit class GenericAction[+R[_], B](actionBuilder: ActionBuilder[R, B]) {
    def generic[F[_], A](bodyParser: BodyParser[A])(block: R[A] => F[Result])(implicit transformer: F ~> Future): Action[A] = actionBuilder.async(bodyParser)(r => transformer(block(r)))
  }
}
