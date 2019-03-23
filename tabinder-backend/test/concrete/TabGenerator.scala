package concrete

import eu.timepit.refined.auto._
import models.Tab
import org.scalacheck.Gen

trait TabGenerator {
  def antoineDufourTabs: List[Tab] = List(
    Tab("Antoine Dufour", "Lost in your Eyes", "EACGBD"),
    Tab("Antoine Dufour", "These Moments", "CGEF#BD#")
  )

  def standardTuningTabs: List[Tab] = List(
    Tab("Kotaro Oshio", "Twilight", "EACGBD"),
    Tab("Paddy Sun", "Sunflower", "Standard"),
    Tab("Beethoven", "Fur Elise", "Standard")
  )

  def tabGenerator: Gen[Tab] = ???
}
