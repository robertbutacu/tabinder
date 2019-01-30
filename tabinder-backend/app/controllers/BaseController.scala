package controllers

import cats.Monad
import play.api.libs.json.Json.toJson
import play.api.libs.json._
import play.api.mvc.Results.Status
import play.api.mvc.{AnyContent, Request, Result}

import scala.concurrent.ExecutionContext
import scala.language.higherKinds

trait BaseController {
  def withValidJson[T, F[_]](f: T => F[Result])(implicit ec: ExecutionContext,
                                                request: Request[AnyContent],
                                                r: Reads[T],
                                                M: Monad[F]): F[Result] = {
    request.body.asJson.map(jsValue => jsValue.validate[T] match {
      case JsSuccess(t, _) => f(t)
      case JsError(_)      => M.pure(Status(400)(toJson("Bad body")))
    }).getOrElse(M.pure(Status(400)(toJson("Missing body"))))
  }
}
