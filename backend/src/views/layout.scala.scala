package views

import scalatags.Text.all.{*, given}
import utils.PageContext

val backgroundSecondary = "#006600"

def layout(inner: Frag)(using ctx: PageContext) = 
  html(
    head(
      title := ctx.title,
      meta(charset := "UTF-8")
    ),
    body(
      padding := "0px",
      margin := "0px",
      border := "0px",

      script(src := "https://unpkg.com/heroicons@2.0.13"),

      div(
        display.flex,
        flexDirection.column,

        // Header
        header(
          div(
            backgroundColor := backgroundSecondary,
            height := "52px",
            width := "100%",

            div(
              display.flex,
              height := "100%",
              width := "100%",
              padding := "10px 40px",
              alignItems.center,

              div(
                span(
                  "Пн-Вс 09:00-18:00"
                )
              ),
              a(
                icon("iron-down-tray"),
                span("Скачать прайс")
              )
            )
          )
        ),

        // Content
        inner
      )
    )
  )