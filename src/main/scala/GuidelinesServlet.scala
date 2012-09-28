import java.awt.Color
import org.scalatra.ScalatraServlet

class GuidelinesServlet extends ScalatraServlet {
    post("/guidelines") {
        val nibSize = params("nibSize") match {
            case "custom" => Some(params("customNibSize").toFloat)
            case s: String => Some(s.toFloat)
            case _ => None
        }
        contentType = "application/pdf"
        response.addHeader("Content-Disposition", "filename=guidelines-%.1f.pdf" format (nibSize.getOrElse(0f)))
        val lines: Array[Line] = Array(
            Line(params.get("descenderHeight").map(_.toFloat * nibSize.get).getOrElse(nibSize.get * 3), 0.5f),
            Line(params.get("ascenderHeight").map(_.toFloat * nibSize.get).getOrElse(nibSize.get * 3), color = Color.decode("#DCDCDC")),
            Line(params.get("bodyHeight").map(_.toFloat * nibSize.get).getOrElse(nibSize.get * 5), color = Color.decode("#DCDCDC"))
        )
        PDFGuidelines(GuidelinesConfig(nibSize), lines).save(response.getOutputStream)
    }
}
