package concrete

import models.types.Types._
import org.scalacheck.Gen
import eu.timepit.refined.auto._
import eu.timepit.refined._
import models.data.Tab

trait TabGenerator {
  def antoineDufourTabs: List[Tab] = List(
    antoineDufourTab,
    Tab("Antoine Dufour", "These Moments", "CGEF#BD#")
  )

  def otherTabs: List[Tab] = List(Tab("Kotaro Oshio", "Twilight", "EACGBD")) ::: standardTuningTabs

  val antoineDufourTab = Tab("Antoine Dufour", "Lost in your Eyes", "EACGBD")
  val standardTuningTabs = List(Tab("Paddy Sun", "Sunflower", "Standard"),
    Tab("Beethoven", "Fur Elise", "Standard"))

  def tabGenerator: Gen[Tab] = {
    for {
      artist   <- artistGenerator
      songname <- songNameGenerator
      tuning   <- tuningGenerator
    } yield Tab(artist, songname, tuning)
  }

  private def tuningGenerator : Gen[Tuning]= {
    val tuningLetterGen: Gen[String] = Gen.oneOf("A","B","C","D","E","F","G")
    val inSharpGen: Gen[String]      = Gen.oneOf("", "#")

    def randomTuningGen: Gen[Tuning] = {
      Gen.const((0 to 5).foldLeft(""){
        (acc, _) =>
          val currentString = tuningLetterGen.sample.get + inSharpGen.sample.get
          acc + currentString
      }).map(s => refineV[ValidTuningFormat](s).right.get)
    }

    Gen.oneOf(randomTuningGen, Gen.const("Standard": Tuning))
  }

  private def artistGenerator: Gen[Artist]     = Gen.alphaStr.map(s => refineV[ValidArtistName](s).right.get)
  private def songNameGenerator: Gen[SongName] = Gen.alphaStr
}
