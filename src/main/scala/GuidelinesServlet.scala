import java.awt.Color
import org.scalatra.ScalatraServlet

class GuidelinesServlet extends ScalatraServlet {
    def param(name: String) = {
        params.get(name).filter(_.trim.nonEmpty)
    }

    post("/guidelines") {
        val nibSize = params("nibSize") match {
            case s: String if !s.isEmpty => Some(s.toFloat)
            case _ => None
        }
        val units = params("units") match {
            case "inches" => Inches.apply _
            case "mm" => Mm.apply _
        }
        val sizeInNibs = params.getOrElse("sizeInNibs", "off") match {
            case "on" => true
            case _ => false
        }
        contentType = "application/pdf"
        response.addHeader("Content-Disposition", "filename=guidelines-%.1f.pdf" format (nibSize.getOrElse(0f)))
        val descender = param("descenderHeight").map(_.toFloat)
        val ascender = param("ascenderHeight").map(_.toFloat)
        val body = param("bodyHeight").map(_.toFloat)
        val lines: Array[Line] = Array(
            Line(if (sizeInNibs) units(descender.getOrElse(3f) * nibSize.get) else units(descender.getOrElse(3 * nibSize.get)), 0.5f),
            Line(if (sizeInNibs) units(ascender.getOrElse(3f) * nibSize.get) else units(ascender.getOrElse(3 * nibSize.get)), color = Color.decode("#DCDCDC")),
            Line(if (sizeInNibs) units(body.getOrElse(5f) * nibSize.get) else units(body.getOrElse(5 * nibSize.get)), color = Color.decode("#DCDCDC"))
        )
        PDFGuidelines(GuidelinesConfig(nibSize), lines).save(response.getOutputStream)
    }
}
