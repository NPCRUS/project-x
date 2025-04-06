import zio.http.{*, given}
import zio.ZIOAppDefault
import scalatags.Text.all.{*, given}
import zio.http.template.Html
import java.io.File
import java.net.URI
import zio.ZIO
import scala.concurrent.Future
import zio.Schedule
import shared.SharedTemplate

object Main extends ZIOAppDefault {

  given PageContext(
    "hello"
  )

  val template = new SharedTemplate(scalatags.Text)
  var state = 0

  val routes = Routes(
    Method.GET / Root -> handler { (request: Request) =>
      Response.html(Html.raw(page.toString))
    },
    Method.GET / "filters" -> handler { (request: Request) =>
      Response.html(Html.raw(filters.toString))
    },
    Method.GET / "hello" -> handler { (request: Request) =>
    //   state = state + 1
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
    //   state = state + 1
      ZIO.unit.delay(zio.Duration.fromMillis(150)).map { _ =>
        Response.text(state.toString)
      }
    },
    Method.GET / "main.js" -> handler { (request: Request) =>
      val file = File("./out/frontend/fastLinkJS.dest/main.js")
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
  override val run = Server.serve(routes).provide(Server.defaultWithPort(5002))
}

case class PageContext(title: String)

case class FilterVariant(id: Int, name: String)
case class Filter(id: Int, name: String, variants: Map[String, Boolean])
val initialFilters = Seq(
  Filter(
    1,
    "Manufacturer",
    Map(
      "Tarkett" -> false,
      "Classen" -> false,
      "Egger" -> false,
      "Kastomonu" -> false,
      "Kronospan" -> false
    )
  ),
  Filter(
    2,
    "Collection",
    Map(
      "Woodstyle" -> false,
      "YellowLine" -> false,
      "AlmostSkies" -> false,
      "Oak" -> false,
      "Seatbelt" -> false,
      "Pivec" -> false,
      "Musor" -> false
    )
  )
)
val relation = Map(
  1 -> ""
)

def filters(using PageContext) = layout(
  div(
    display.flex,
    flexDirection.column,
    width := "200px",
    initialFilters.map { f =>
      filter(f)
    }
  )
)

def filter(f: Filter) = frag(
  div(
    display.flex,
    justifyContent.spaceBetween,
    backgroundColor := "gray",
    p(f.name),
    p("collapse")
  ),
  f.variants.toSeq.map { (key, checked) =>
    div(
      display.flex,
      flexDirection.column,
      div(
        input(
          `type` := "checkbox"
        ),
        label(key)
      )
    )
  }
)

def page(using PageContext) = layout(
  frag(
    // Categories
    tag("section")(
      cls := "mb-4",
      h2(cls := "text-center mb-3")("Categories"),
      div(
        cls := "row",
        div(
          cls := "col-md-3",
          div(
            cls := "card",
            div(
              cls := "card-body",
              h5(cls := "card-title")("Hardwood Flooring"),
              p(cls := "card-text")("Explore our premium hardwood options.")
            )
          )
        ),
        div(
          cls := "col-md-3",
          div(
            cls := "card",
            div(
              cls := "card-body",
              h5(cls := "card-title")("Laminate Flooring"),
              p(cls := "card-text")("Durable and stylish laminate solutions.")
            )
          )
        ),
        div(
          cls := "col-md-3",
          div(
            cls := "card",
            div(
              cls := "card-body",
              h5(cls := "card-title")("Vinyl Flooring"),
              p(cls := "card-text")("Affordable and versatile vinyl options.")
            )
          )
        ),
        div(
          cls := "col-md-3",
          div(
            cls := "card",
            div(
              cls := "card-body",
              h5(cls := "card-title")("Carpet Flooring"),
              p(cls := "card-text")("Soft and cozy carpet choices.")
            )
          )
        )
      )
    ),

    // Services
    tag("section")(
      cls := "mb-4",
      h2(cls := "text-center mb-3")("Our Services"),
      div(
        cls := "row",
        div(
          cls := "col-md-4",
          div(
            cls := "card",
            div(
              cls := "card-body",
              h5(cls := "card-title")("Free Consultation"),
              p(cls := "card-text")(
                "Get expert advice on the best flooring for your space."
              )
            )
          )
        ),
        div(
          cls := "col-md-4",
          div(
            cls := "card",
            div(
              cls := "card-body",
              h5(cls := "card-title")("Installation Services"),
              p(cls := "card-text")(
                "Professional installation for all flooring types."
              )
            )
          )
        ),
        div(
          cls := "col-md-4",
          div(
            cls := "card",
            div(
              cls := "card-body",
              h5(cls := "card-title")("Maintenance & Repair"),
              p(cls := "card-text")(
                "Keep your floors looking new with our maintenance services."
              )
            )
          )
        )
      )
    ),

    // Most Popular Goods
    tag("section")(
      cls := "mb-4",
      h2(cls := "text-center mb-3")("Most Popular Products"),
      div(
        cls := "row",
        div(
          cls := "col-md-4",
          div(
            cls := "card",
            img(
              cls := "card-img-top",
              src := "https://via.placeholder.com/150",
              alt := "Product 1"
            ),
            div(
              cls := "card-body",
              h5(cls := "card-title")("Oak Hardwood Flooring"),
              p(cls := "card-text")(
                "Classic oak flooring with a natural finish."
              ),
              a(href := "#", cls := "btn btn-primary")("View Details")
            )
          )
        ),
        div(
          cls := "col-md-4",
          div(
            cls := "card",
            img(
              cls := "card-img-top",
              src := "https://via.placeholder.com/150",
              alt := "Product 2"
            ),
            div(
              cls := "card-body",
              h5(cls := "card-title")("Waterproof Laminate Flooring"),
              p(cls := "card-text")("Perfect for high-moisture areas."),
              a(href := "#", cls := "btn btn-primary")("View Details")
            )
          )
        ),
        div(
          cls := "col-md-4",
          div(
            cls := "card",
            img(
              cls := "card-img-top",
              src := "https://via.placeholder.com/150",
              alt := "Product 3"
            ),
            div(
              cls := "card-body",
              h5(cls := "card-title")("Luxury Vinyl Planks"),
              p(cls := "card-text")("Elegant and durable vinyl planks."),
              a(href := "#", cls := "btn btn-primary")("View Details")
            )
          )
        )
      )
    ),

    // Popular Manufacturers
    tag("section")(
      cls := "mb-4",
      h2(cls := "text-center mb-3")("Popular Manufacturers"),
      div(
        cls := "row",
        div(
          cls := "col-md-3",
          div(
            cls := "card",
            img(
              cls := "card-img-top",
              src := "https://via.placeholder.com/100",
              alt := "Manufacturer 1"
            ),
            div(
              cls := "card-body",
              h5(cls := "card-title")("Brand A"),
              p(cls := "card-text")("High-quality flooring solutions.")
            )
          )
        ),
        div(
          cls := "col-md-3",
          div(
            cls := "card",
            img(
              cls := "card-img-top",
              src := "https://via.placeholder.com/100",
              alt := "Manufacturer 2"
            ),
            div(
              cls := "card-body",
              h5(cls := "card-title")("Brand B"),
              p(cls := "card-text")("Innovative and stylish flooring.")
            )
          )
        ),
        div(
          cls := "col-md-3",
          div(
            cls := "card",
            img(
              cls := "card-img-top",
              src := "https://via.placeholder.com/100",
              alt := "Manufacturer 3"
            ),
            div(
              cls := "card-body",
              h5(cls := "card-title")("Brand C"),
              p(cls := "card-text")("Eco-friendly flooring options.")
            )
          )
        ),
        div(
          cls := "col-md-3",
          div(
            cls := "card",
            img(
              cls := "card-img-top",
              src := "https://via.placeholder.com/100",
              alt := "Manufacturer 4"
            ),
            div(
              cls := "card-body",
              h5(cls := "card-title")("Brand D"),
              p(cls := "card-text")("Durable and affordable flooring.")
            )
          )
        )
      )
    )
  )
)

def layout(inner: Frag)(using ctx: PageContext) =
  html(
    head(
      title := ctx.title,
      link(
        rel := "stylesheet",
        href := "https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"
      ),
      script(
        src := "/main.js"
      )
    ),
    body(
      div(
        cls := "container",

        // Header
        header(
          cls := "text-center my-4",
          h1(cls := "display-4")("Flooring Solutions"),
          p(cls := "lead")("Your one-stop shop for all flooring needs")
        ),

        // Global Search
        div(
          cls := "row justify-content-center mb-4",
          div(
            cls := "col-md-6",
            form(
              cls := "form-inline",
              input(
                cls := "form-control mr-2",
                `type` := "text",
                placeholder := "Search for products..."
              ),
              button(cls := "btn btn-primary", `type` := "submit")("Search")
            )
          )
        ),

        // Content
        inner
      )
    )
  )
