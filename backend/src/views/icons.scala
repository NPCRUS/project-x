package views

import scalatags.Text.all.*
import scalatags.Text.svgTags.*
import scalatags.Text.svgAttrs.*

private def icon(name: String) = 
  svg(
    cls := "w-6 h-6",

    use(href := s"#heroicon-$name")
  )


