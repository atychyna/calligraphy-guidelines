import java.awt.Color
import java.io.{FileOutputStream, File}
import org.scalatest.FunSuite

class PDFGenerationSuite extends FunSuite {
    test("test PDF file is generated") {
        val pdf = PDFGuidelines(GuidelinesConfig(Some(1f / 8)))
        val tmpFile = File.createTempFile("pdf-guidelines", ".pdf")
        pdf.save(new FileOutputStream(tmpFile))
        assert(tmpFile.exists())
        assert(tmpFile.length() > 0)
    }

    test("test PDF file with custom lines config") {
        val lines: Array[Line] = Array(
            Line(Mm(10f), 0.5f),
            Line(Mm(20f), color = Color.decode("#DCDCDC")),
            Line(Mm(10f), color = Color.decode("#DCDCDC"))
        )
        val pdf = PDFGuidelines(GuidelinesConfig(), lines)
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
            PDFGuidelines(GuidelinesConfig(), null)
        }
    }
}
