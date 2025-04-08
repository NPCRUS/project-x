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
    Method.GET / "hello" -> handler { (request: Request) =>
      state = state + 1
      Response.html(Html.raw(
        layout(frag(
          button("refresh", id := "refresh"),
          div(
            id := "inner",
            template.renderHello(state)
          )
        )).toString
      ))
    },
    Method.GET / "hello" / "api" -> handler { (request: Request) =>
      state = state + 1
      ZIO.unit.delay(zio.Duration.fromMillis(150)).map { _ =>
        Response.text(state.toString)
      }
    },
    Method.GET / "main.js" -> handler { (request: Request) =>
      val file = File("/Users/nikitaglushchenko/projects/private/poor-maxim/out/frontend/fastLinkJS.dest/main.js")
      Body.fromFile(file).map { body =>
        Response(
          headers = Headers.apply(Header.ContentType(MediaType.forContentType("application/json").get)),
          body = body
        )
      }
    }.catchAll {
          case _ => Handler.internalServerError
        }
  )

  // Run it like any simple app
  override val run = Server.serve(routes).provide(Server.defaultWith(c =>
    c.copy(gracefulShutdownTimeout = Duration.fromMillis(0))
        .port(5001)
  ))
}