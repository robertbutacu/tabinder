package controllers

import cats.{Monad, MonadError}
import logger.MLogger
import play.api.libs.json.Json.toJson
import play.api.libs.json._
import play.api.mvc.Results.Status
import play.api.mvc.{AnyContent, Request, Result}

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

trait BaseController[F[_]] {
  def logger: MLogger[F]

  def withValidJson[T](f: T => F[Result])(implicit ec: ExecutionContext,
                                                request: Request[AnyContent],
                                                r: Reads[T],
                                                M: Monad[F]): F[Result] = {
    request.body.asJson.map(jsValue => jsValue.validate[T] match {
      case JsSuccess(t, _) => f(t)
      case JsError(e)      =>
        logger.error("Failed parsing request with: " + e.toString())
        M.pure(Status(400)(toJson("Bad body")))
    }).getOrElse(M.pure(Status(400)(toJson("Missing body"))))
  }

  def withRecover[T](f: => F[Result])(implicit ec: ExecutionContext,
                                            M: MonadError[F, Throwable]): F[Result] = {
    M.recoverWith(f) {
      case exp: JsResultException =>
        logger.error("Failed with: " + exp.errors.toString())
        M.pure(Status(500)("Something went wrong! Ooups!"))
    }
  }
}
