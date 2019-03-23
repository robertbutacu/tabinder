package services

import concrete.TabRepositoryTest.{HappyPathRepository, MutableRepository}
import logger.PlayLogger
import org.scalatest.{FlatSpec, Matchers}
import cats.instances.all._
import concrete.TabGenerator

import scala.util.Try

class TabServiceTest extends FlatSpec with Matchers with TabGenerator {
  val logger              = new PlayLogger[Try]
  val allTabs             = standardTuningTabs ::: antoineDufourTabs
  val happyPathRepository = new HappyPathRepository(allTabs)
  val mutableRepository   = new MutableRepository()

  "Tab service" should "return all tabs from repository" in {
    val service = new TabService[Try](happyPathRepository, logger)

    service.getAll().get shouldBe allTabs
  }
}
