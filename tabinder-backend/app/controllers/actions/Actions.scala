package controllers.actions

import cats.{Monad, ~>}
import javax.inject.Inject
import play.api.mvc._
import utils.Utils.AbstractGenericController
import utils.filter.{GenericActionFilter, GenericActionRefiner}

import scala.concurrent.{ExecutionContext, Future}
import scala.language.higherKinds

object Actions {

  case class RequestWithProviderId[A](providerId: String, request: Request[A]) extends WrappedRequest[A](request)

  class RequestFiltered[F[_] : Monad] @Inject()(implicit val executionContext: ExecutionContext,
                                        val transformer: F ~> Future) extends GenericActionFilter[F, Request] {

    override def genericFilter[A](request: Request[A]): F[Option[Result]] =
      implicitly[Monad[F]].pure(None)
  }

  class ExtraRequest[F[+_]] @Inject()(implicit val executionContext: ExecutionContext,
                              val transformer: F ~> Future, M: Monad[F])
    extends GenericActionRefiner[F, Request, RequestWithProviderId] {

    override def genericRefine[A](request: Request[A]): F[Either[Result, RequestWithProviderId[A]]] = {
      M.pure(Right(RequestWithProviderId("some-provider-id", request)))
    }
  }

  class ComposedActions[F[+_]]@Inject() (
                                requestFiltered: RequestFiltered[F],
                                extraRequest: ExtraRequest[F],
                                cc: ControllerComponents
                              )(implicit M: Monad[F], transformer: F ~> Future) extends AbstractGenericController(cc) {
    def filtered: ActionBuilder[RequestWithProviderId, AnyContent] = Action andThen requestFiltered andThen extraRequest
  }
}
