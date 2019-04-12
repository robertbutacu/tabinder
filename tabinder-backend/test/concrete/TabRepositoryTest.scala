package concrete

import cats.Applicative
import cats.data.State
import models.Tab
import models.types.Types.{Artist, SongName, Tuning}
import repositories.TabRepositoryAlgebra
import State._
import scala.language.higherKinds
import scala.util.Try

object TabRepositoryTest {
  import cats.instances.all._

  class HappyPathRepository(tabs: List[Tab]) extends TabRepositoryAlgebra[Try] with TabGenerator {
    override def create(tab: Tab): Try[Unit] = Applicative[Try].pure(Unit)
    override def remove(tab: Tab): Try[Unit] = Applicative[Try].pure(Unit)

    override def getByArtist(artist: Artist): Try[List[Tab]]   = Try(tabs.filter(_.artist == artist))
    override def getByTuning(tuning: Tuning): Try[List[Tab]]   = Try(tabs.filter(_.tuning == tuning))
    override def getBySong(songName: SongName): Try[List[Tab]] = Try(tabs.filter(_.songName == songName))
    override def getAll: Try[List[Tab]] = Try(tabs)

    override def getAllArtists: Try[Set[Artist]] = ???

    override def getAllTunings: Try[Set[Tuning]] = ???

    override def getAllSongs: Try[Set[SongName]] = ???
  }

  type MutableRepositoryType[T] = State[List[Tab], T]

  class MutableRepository extends TabRepositoryAlgebra[MutableRepositoryType] {
    private val internalState: MutableRepositoryType[Unit] = State { tabs => (tabs, ())}

    override def create(tab: Tab): MutableRepositoryType[Unit] = internalState.modify(tabs => tabs :+ tab)
    override def remove(tab: Tab): MutableRepositoryType[Unit] = internalState.modify(tabs => tabs.filter(_ == tab))

    override def getByArtist(artist: Artist): MutableRepositoryType[List[Tab]] = {
      internalState.inspect(tabs => tabs.filter(_.artist == artist))
    }

    override def getByTuning(tuning: Tuning): MutableRepositoryType[List[Tab]] = {
      internalState.inspect(tabs => tabs.filter(_.tuning == tuning))
    }

    override def getBySong(songName: SongName): MutableRepositoryType[List[Tab]] = {
      internalState.inspect(tabs => tabs.filter(_.songName == songName))
    }

    override def getAll: MutableRepositoryType[List[Tab]] = internalState.get

    override def getAllArtists: MutableRepositoryType[Set[Artist]] = ???

    override def getAllTunings: MutableRepositoryType[Set[Tuning]] = ???

    override def getAllSongs: MutableRepositoryType[Set[SongName]] = ???
  }
}
