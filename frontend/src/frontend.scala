import org.scalajs.dom
import scala.scalajs.js.JSON

import scala.concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom.Request
import org.scalajs.dom.RequestInit
import org.scalajs.dom.HttpMethod
import scala.concurrent.Future

val template = SharedTemplate(scalatags.JsDom)

@main
def frontendApp(): Unit = {
  println("hello world")

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
