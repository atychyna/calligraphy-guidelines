import java.awt.Color
import java.io.OutputStream
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream
import org.apache.pdfbox.pdmodel.{PDPage, PDDocument}

sealed abstract class Unit {
    val userSpaceUnitDPI = 72

    def units: Float

    def value: Float

    def create(value: Float): Unit

    def *(f: Float) = create(value * f)
}

case class Mm(mm: Float) extends Unit {
    val value = mm
    val units = (value / 25.4f) * userSpaceUnitDPI

    def create(value: Float) = new Mm(value)
}

case class Inches(inches: Float) extends Unit {
    val value = inches
    val units = inches * userSpaceUnitDPI

    def create(value: Float) = new Inches(value)
}

case class Line(distance: Unit, width: Float = 1f, color: Color = Color.decode("#ECECEC"))

class PDFGuidelines(val lines: Array[Line]) extends Guidelines {
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

    def apply(nib: Unit, ascender: Float, body: Float, descender: Float) = {
        require(nib != null, "Nib should be provided")
        val lines: Array[Line] = Array(
            Line(nib * descender, 0.5f),
            Line(nib * ascender, color = Color.decode("#DCDCDC")),
            Line(nib * body, color = Color.decode("#DCDCDC"))
        )
        new PDFGuidelines(lines)
    }

    def apply(lines: Array[Line]) = {
        new PDFGuidelines(lines)
    }
}
