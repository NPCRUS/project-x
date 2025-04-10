import zio.http.{*, given}
import zio.ZIOAppDefault
import scalatags.Text.all.{*, given}
import zio.http.template.Html
import java.io.File
import java.net.URI
import zio.ZIO
import scala.concurrent.Future
import zio.Schedule
import utils.PageContext
import views.*
import zio.Duration
import java.io.FileWriter
import zio.http.codec.PathCodec.segment

object Main extends ZIOAppDefault {

  given PageContext(
    "hello"
  )

  val template = new SharedTemplate(scalatags.Text)
  var state = 0

  val routes = Routes(
    Method.GET / Root -> handler { (request: Request) =>
      Response.html(Html.raw(layout(h1("hello world")).toString))
    },
    Method.GET / "dest" / trailing -> handler { (request: Request) =>
      val path = request.url.path.toString.stripPrefix("/dest/")
      val file = File(s"dest/$path")
      
      if (file.exists() && file.isFile) {
        val contentType = path.split('.').lastOption match {
          case Some("js") => "application/javascript"
          case Some("css") => "text/css"
          case Some("html") => "text/html"
          case Some("json") => "application/json"
          case Some("png") => "image/png"
          case Some("jpg") | Some("jpeg") => "image/jpeg"
          case Some("svg") => "image/svg+xml"
          case _ => "application/octet-stream"
        }
        
        Body.fromFile(file).map { body =>
          Response(
            headers = Headers(Header.ContentType(MediaType.forContentType(contentType).get)),
            body = body
          )
        }
      } else {
        ZIO.succeed(Response.notFound)
      }
    }.catchAll {
      case _ => Handler.internalServerError
    }
  )

  override val run = Server.serve(routes).provide(Server.defaultWith(c =>
    c.copy(gracefulShutdownTimeout = Duration.fromMillis(0))
        .port(5001)
  ))
}