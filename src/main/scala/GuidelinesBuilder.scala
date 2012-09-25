import java.io.OutputStream

abstract class Guidelines(config: GuidelinesConfig) {
  def save(out:OutputStream)
}

case class GuidelinesConfig(nib: Float)