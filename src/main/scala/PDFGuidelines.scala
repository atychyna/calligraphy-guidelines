import java.awt.Color
import java.io.OutputStream
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream
import org.apache.pdfbox.pdmodel.{PDPage, PDDocument}

case class Line(distance: Float, width: Float = 1f, color: Color = Color.decode("#ECECEC"))

class PDFGuidelines(val config: GuidelinesConfig, val lines: Array[Line]) extends Guidelines(config) {
    require(config != null, "GuidelinesConfig should be provided")
    require(lines != null, "Please specify lines to draw")

    def save(out: OutputStream) {
        val pdf = new PDDocument
        val page = new PDPage(PDPage.PAGE_SIZE_A4)
        pdf.addPage(page)
        val stream = new PDPageContentStream(pdf, page)
        val box = page.findMediaBox
        val segmentsCount = (box.getHeight / segmentHeight).toInt
        var distance = box.getHeight
        for {i <- 0 to segmentsCount
             l <- 0 until lines.length} {
            val line = lines(l)
            distance -= inchesToUnits(line.distance)
            stream.setLineWidth(line.width)
            stream.setStrokingColor(line.color)
            stream.drawLine(0, distance, box.getWidth, distance)
        }
        stream.close()
        pdf.save(out)
        pdf.close()
    }

    val userSpaceUnitDPI = 72

    private def inchesToUnits(inches: Float) = inches * userSpaceUnitDPI

    private def mmToUnits(mm: Float) = 1 / (10 * 2.54f) * userSpaceUnitDPI

    private def segmentHeight = inchesToUnits((0f /: lines)(_ + _.distance))
}

object PDFGuidelines {

    def apply(config: GuidelinesConfig) = {
        val lines: Array[Line] = Array(
            Line(config.descender.orElse(config.nib.map(_ * 3)).get, 0.5f),
            Line(config.descender.orElse(config.nib.map(_ * 3)).get, color = Color.decode("#DCDCDC")),
            Line(config.descender.orElse(config.nib.map(_ * 5)).get, color = Color.decode("#DCDCDC"))
        )
        new PDFGuidelines(config, lines)
    }

    def apply(config: GuidelinesConfig, lines: Array[Line]) = {
        new PDFGuidelines(config, lines)
    }
}
