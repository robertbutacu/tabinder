package controllers

import cats.Monad
import cats.syntax.applicative._
import logger.MLogger
import models.types.Types.ThrowableMonadError
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
        Status(400)(toJson("Bad body")).pure
    }).getOrElse(Status(400)(toJson("Missing body")).pure)
  }

  //TODO refactor so JsResultException does not get here
  def withRecover[T](f: => F[Result])(implicit ec: ExecutionContext,
                                            M: ThrowableMonadError[F]): F[Result] = {
    M.recoverWith(f) {
      case exp: JsResultException =>
        logger.error("Failed parsing json with: " + exp.errors.toString())
        Status(500)("Something went wrong! Ooups!").pure
      case e: Throwable           =>
        logger.error("Fatal error with: " + e.toString)
        Status(500)("Something went wrong! Don't contact us, we'll contact you.").pure
    }
  }
}
