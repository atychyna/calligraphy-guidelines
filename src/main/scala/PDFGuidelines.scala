import java.awt.Color
import java.io.OutputStream
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream
import org.apache.pdfbox.pdmodel.{PDPage, PDDocument}

sealed abstract class Unit {
    val userSpaceUnitDPI = 72

    def units: Float
}

case class Mm(mm: Float) extends Unit {
    val units = mmToUnits(mm)

    private def mmToUnits(mm: Float) = (mm / 25.4f) * userSpaceUnitDPI
}

case class Inches(inches: Float) extends Unit {
    val units = inchesToUnits(inches)

    private def inchesToUnits(inches: Float) = inches * userSpaceUnitDPI
}

case class Line(distance: Unit, width: Float = 1f, color: Color = Color.decode("#ECECEC"))

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
            distance -= line.distance.units
            stream.setLineWidth(line.width)
            stream.setStrokingColor(line.color)
            stream.drawLine(0, distance, box.getWidth, distance)
        }
        stream.close()
        pdf.save(out)
        pdf.close()
    }


    private def segmentHeight = (0f /: lines)(_ + _.distance.units)
}

object PDFGuidelines {

    def apply(config: GuidelinesConfig) = {
        require(config != null, "GuidelinesConfig should be provided")
        val lines: Array[Line] = Array(
            Line(Inches(config.descender.orElse(config.nib.map(_ * 3)).get), 0.5f),
            Line(Inches(config.descender.orElse(config.nib.map(_ * 3)).get), color = Color.decode("#DCDCDC")),
            Line(Inches(config.descender.orElse(config.nib.map(_ * 5)).get), color = Color.decode("#DCDCDC"))
        )
        new PDFGuidelines(config, lines)
    }

    def apply(config: GuidelinesConfig, lines: Array[Line]) = {
        new PDFGuidelines(config, lines)
    }
}
