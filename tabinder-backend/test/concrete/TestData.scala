package concrete

import models.data.Tab
import eu.timepit.refined.auto._

trait TestData extends TabGenerator {
  def antoineDufourTabs: List[Tab] = List(
    antoineDufourTab,
    Tab("Antoine Dufour", "These Moments", "CGEF#BD#")
  )

  def otherTabs: List[Tab] = List(Tab("Kotaro Oshio", "Twilight", "EACGBD")) ::: standardTuningTabs

  val antoineDufourTab   = Tab("Antoine Dufour", "Lost in your Eyes", "EACGBD")
  val standardTuningTabs = List(Tab("Paddy Sun", "Sunflower", "Standard"),
    Tab("Beethoven", "Fur Elise", "Standard"))

}
