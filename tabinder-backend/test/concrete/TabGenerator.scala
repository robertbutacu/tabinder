package concrete

import models.types.Types._
import org.scalacheck.Gen
import eu.timepit.refined.auto._
import eu.timepit.refined._
import models.data.Tab

trait TabGenerator {
  def tabGenerator: Gen[Tab] = {
    for {
      artist   <- artistGenerator
      songname <- songNameGenerator
      tuning   <- tuningGenerator
    } yield Tab(artist, songname, tuning)
  }

  private def tuningGenerator : Gen[Tuning]= {
    val tuningLetterGen: Gen[String] = Gen.oneOf("A","B","C","D","E","F","G")
    val inSharpGen:      Gen[String] = Gen.oneOf("", "#")

    def randomTuningGen: Gen[Tuning] = {
      Gen.const((0 to 5).foldLeft(""){
        (acc, _) =>
          val currentString = tuningLetterGen.sample.get + inSharpGen.sample.get
          acc + currentString
      }).map(s => refineV[ValidTuningFormat](s).right.get)
    }

    Gen.oneOf(randomTuningGen, Gen.const("Standard": Tuning))
  }

  private def artistGenerator:   Gen[Artist]   = Gen.alphaStr.map(s => refineV[ValidArtistName](s).right.get)
  private def songNameGenerator: Gen[SongName] = Gen.alphaStr
}
