package property

import cats.Traverse
import concrete.TabRepositoryTest.{MutableRepository, MutableRepositoryType}
import models.data.Tab
import org.scalacheck.Properties
import org.scalacheck.Prop.forAll
import services.TabService
import cats.instances.all._
import concrete.{SilentLog, TabGenerator}
import cats.syntax.all._

class TabServicePropertySpec extends Properties("TabServiceAlgebra") with TabGenerator {

  property("a posted tab can then be retrieved") = forAll { (t: Tab, otherTabs: List[Tab]) =>
    val tabs = otherTabs :+ t
    withRepositorySetup(tabs) {
      service =>
        val results = for {
          _                 <- Traverse[List].sequence(tabs.map(t => service.post(t)))
          retrievalBySong   <- service.getBySong(t.songName)
          retrievalByTuning <- service.getByTuning(t.tuning)
          retrievalByArtist <- service.getByArtist(t.artist)
        } yield (retrievalBySong, retrievalByTuning, retrievalByArtist)

        val (_, (retrievalBySong, retrievalByTuning, retrievalByArtist)) = results.runEmpty.value

        retrievalBySong.contains(t) && retrievalByTuning.contains(t) && retrievalByArtist.contains(t)
    }
  }

  property("a deleted tab cannot be retrieved") = forAll { (t: Tab, otherTabs: List[Tab]) =>
    val tabs = otherTabs :+ t
    withRepositorySetup(tabs) {
      service =>
        val results = for {
          _            <- Traverse[List].sequence(tabs.map(t => service.post(t)))
          persisted    <- service.getAll
          _            <- service.delete(t)
          notPersisted <- service.getAll
        } yield (persisted, notPersisted)

        val (_, (persisted, notPersisted)) = results.runEmpty.value

        persisted.contains(t) && notPersisted.forall(x => x =!= t)
    }
  }

  def withRepositorySetup[A](tabs: List[Tab])(test: TabService[MutableRepositoryType] => A): A = {
    val repository = new MutableRepository
    val service    = new TabService[MutableRepositoryType](repository, new SilentLog[MutableRepositoryType]())

    test(service)
  }
}
