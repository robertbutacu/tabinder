package logger

import cats.Monad

import scala.language.higherKinds

trait MLogger[F[_]] {
  def error(s: String):   F[Unit]
  def message(s: String): F[Unit]
  def warning(s: String): F[Unit]
}

class PrintoutMLogger[F[_]](implicit M: Monad[F]) extends MLogger[F] {
  override def error(s: String): F[Unit]   = M.pure {
    println(s"[Error] $s")
  }

  override def message(s: String): F[Unit] = M.pure {
    println(s"[Message] $s")
  }

  override def warning(s: String): F[Unit] = M.pure {
    println(s"[Warning] $s")
  }
}