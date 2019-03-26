package services

import cats.Functor
import concrete.TabRepositoryTest.{HappyPathRepository, MutableRepository}
import logger.PlayLogger
import org.scalatest.{FlatSpec, Matchers}
import cats.instances.all._
import concrete.TabGenerator
import eu.timepit.refined.auto._
import models.Tab

import scala.util.Try

class TabServiceTest extends FlatSpec with Matchers with TabGenerator {
  val logger              = new PlayLogger[Try]
  val allTabs             = standardTuningTabs ::: antoineDufourTabs
  val happyPathRepository = new HappyPathRepository(allTabs)
  val mutableRepository   = new MutableRepository()
  val service = new TabService[Try](happyPathRepository, logger)

  "Tab service" should "return all tabs from repository" in {
    service.getAll().get shouldBe allTabs
  }

  "Tab service" should "return filtered tabs" in {
    service.getByArtist("Antoine Dufour").get shouldBe antoineDufourTabs
  }

  "Tab service" should "return empty list for unknown artists" in {
    service.getByArtist("unknown artist").get shouldBe List.empty[Tab]
  }

  "Tab service" should "return the correct song" in {
    service.getBySong(antoineDufourTab.songName).get shouldBe List(antoineDufourTab)
  }

  "Tab service" should "return empty list for unknown song" in {
    service.getBySong("unknown song").get shouldBe List.empty[Tab]
  }

  "Tab service" should "return correct songs by tuning" in {
    service.getByTuning("Standard").get shouldBe standardTuningTabs
  }

  "Tab service" should "return empty list if it doesnt exist" in {
    service.getByTuning("DADGAD").get shouldBe List.empty[Tab]
  }
}
