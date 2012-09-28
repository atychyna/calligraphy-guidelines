import java.io.OutputStream

abstract class Guidelines(config: GuidelinesConfig) {
    def save(out: OutputStream)
}

case class GuidelinesConfig(nib: Option[Float] = None,
                            descender: Option[Float] = None,
                            body: Option[Float] = None,
                            ascender: Option[Float] = None)