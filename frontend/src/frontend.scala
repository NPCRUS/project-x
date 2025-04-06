import org.scalajs.dom
import scala.scalajs.js.JSON

import scala.concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom.Request
import org.scalajs.dom.RequestInit
import org.scalajs.dom.HttpMethod
import scala.concurrent.Future
import library.Core
import shared.SharedTemplate
import library.Component

val template = SharedTemplate(scalatags.JsDom)
val component = template.renderHello

enum HelloAction {
    case
    INCREMENT,
    DECREMENT,
    PRINT
}

object HelloComponent extends Component[Int, HelloAction] (template.renderHello) {
    def reduce(state: Int, action: HelloAction): Int = action match {
        case HelloAction.INCREMENT => state + 1
        case HelloAction.DECREMENT => state - 1
        case HelloAction.PRINT => {
            println(s"HelloPrint: $state")
            state
        }
    }
}

@main
def frontendApp(): Unit = {
  println("hello world")
  val core = Core

  core.register(
    "inner",
    HelloComponent,
    0,
    HelloAction.values.toSet,
    _.toString,
    HelloAction.valueOf
  )

  dom.window.onload = ev => {
    val button = dom.document.getElementById("refresh")
    val inner = dom.document.getElementById("inner")

    button.addEventListener("click", ev => {
      inner.innerHTML = "fetching"
      dom.fetch( 
        "http://localhost:5002/hello/api",
        new RequestInit {
          method = HttpMethod.GET
        }
      ).toFuture
        .flatMap(_.text().toFuture)
        .map { r =>
          inner.innerHTML = "drawing"
          val state = r.toInt
          val t = template.renderHello(state).render
          inner.innerHTML = ""
          inner.appendChild(t)
        }
    })
  }
}
