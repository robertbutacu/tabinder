package models.types

import cats.MonadError
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean.Or
import eu.timepit.refined.char.{LowerCase, UpperCase, Whitespace}
import eu.timepit.refined.collection.Forall
import eu.timepit.refined.string.MatchesRegex
import scala.language.higherKinds

object Types {
  type ThrowableMonadError[F[_]] = MonadError[F, Throwable]
  type Artist   = Refined[String, ValidArtistName]
  type SongName = String
  type Tuning   = Refined[String, ValidTuningFormat]

  type ValidArtistName   = Forall[Or[UpperCase, Or[LowerCase, Whitespace]]]
  type ValidTuningFormat = MatchesRegex[W.`"""([ABCDEFG](#)?){6}"""`.T]
}
