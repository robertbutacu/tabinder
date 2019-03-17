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
  class HappyPathRepository extends TabRepositoryAlgebra[Try] with TabGenerator {
    override def create(tab: Tab): Try[Unit] = Applicative[Try].pure(Unit)

    override def remove(tab: Tab): Try[Unit] = Applicative[Try].pure(Unit)

    override def getByArtist(artist: Artist): Try[List[Tab]] = {
      if(artist.value == "Antoine Dufour") Try(antoineDufourTabs)
      else                                 Try(List.empty[Tab])
    }

    override def getByTuning(tuning: Tuning): Try[List[Tab]] = {
      if(tuning.value == "Standard" || tuning.value == "EADGBD") Try(standardTuningTabs)
      else                                                       Try(List.empty[Tab])
    }

    override def getBySong(songName: SongName): Try[List[Tab]] = ???

    override def getAll(): Try[List[Tab]] = {
      Try(standardTuningTabs ::: antoineDufourTabs)
    }
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

    override def getAll(): MutableRepositoryType[List[Tab]] = internalState.get
  }
}
