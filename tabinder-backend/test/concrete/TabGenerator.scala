package concrete

import models.Tab
import models.types.Types.Tuning
import org.scalacheck.Gen
import eu.timepit.refined.auto._

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
      })
    }

    Gen.oneOf(randomTuningGen, Gen.const("Standard": Tuning))
  }

  private def artistGenerator: Gen[String]   = Gen.alphaStr
  private def songNameGenerator: Gen[String] = Gen.alphaStr
}
