import java.awt.Color
import java.io.OutputStream
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream
import org.apache.pdfbox.pdmodel.{PDPage, PDDocument}

case class Line(distanceInNibs: Int, width: Float = 1f, color: Color = Color.decode("#ECECEC"))

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
            distance -= inchesToUnits(line.distanceInNibs * config.nib)
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

    private def segmentHeight = inchesToUnits((0f /: lines)(_ + _.distanceInNibs * config.nib))
}

object PDFGuidelines {
    val lines: Array[Line] = Array(
        Line(3, 0.5f),
        Line(3, color = Color.decode("#DCDCDC")),
        Line(5, color = Color.decode("#DCDCDC"))
    )

    def apply(config: GuidelinesConfig) = {
        new PDFGuidelines(config, lines)
    }

    def apply(config: GuidelinesConfig, lines: Array[Line]) = {
        new PDFGuidelines(config, lines)
    }
}
