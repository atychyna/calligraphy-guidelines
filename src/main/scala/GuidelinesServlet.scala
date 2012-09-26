import org.scalatra.ScalatraServlet

class GuidelinesServlet extends ScalatraServlet {
    post("/guidelines") {
        val nibSize = params("nibSize") match {
            case "custom" => params("customNibSize").toFloat
            case s: String => s.toFloat
        }
        contentType = "application/pdf"
        response.addHeader("Content-Disposition", "filename=guidelines-%.1f.pdf" format (nibSize))
        PDFGuidelines(GuidelinesConfig(nibSize)).save(response.getOutputStream)
    }
}
