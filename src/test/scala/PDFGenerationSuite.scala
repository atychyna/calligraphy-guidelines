import java.io.{FileOutputStream, File}
import org.scalatest.FunSuite

class PDFGenerationSuite extends FunSuite {
    test("test PDF file is generated") {
        val pdf = PDFGuidelines(GuidelinesConfig(1f / 8))
        val tmpFile = File.createTempFile("pdf-guidelines", ".pdf")
        pdf.save(new FileOutputStream(tmpFile))
        assert(tmpFile.exists())
        assert(tmpFile.length() > 0)
    }

    test("null config input should throw IllegalArgumentException") {
        intercept[IllegalArgumentException] {
            PDFGuidelines(null)
        }
        intercept[IllegalArgumentException] {
            PDFGuidelines(GuidelinesConfig(2), null)
        }
    }
}
