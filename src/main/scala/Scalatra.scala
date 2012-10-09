import org.scalatra.LifeCycle
import javax.servlet.ServletContext

class Scalatra extends LifeCycle {

    override def init(context: ServletContext) {

        // mount servlets like this:
        context mount(new GuidelinesServlet, "/generate")

        // set init params like this:
        // org.scalatra.cors.allowedOrigins = "http://example.com"
    }
}