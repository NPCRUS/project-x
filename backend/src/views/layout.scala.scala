package views

import scalatags.Text.all.{*, given}
import utils.PageContext
import scalatags.Text.styles

val backgroundSecondary = "#006600"
val buttonSecondary = "#00913d"
val white = "#fff"
val lightGray = "#d3d3d3"
val primaryGray = "#ab9f91"

def xA(modifiers: Modifier*) = a(
  Seq(textDecoration.underline, cursor.pointer) ++ modifiers*
)

def layout(inner: Frag)(using ctx: PageContext) =
  html(
    head(
      title := ctx.title,
      meta(charset := "UTF-8"),
      link(
        rel := "stylesheet",
        href := "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
      )
    ),
    body(
      padding := "0px",
      margin := "0px",
      border := "0px",
      div(
        display.flex,
        flexDirection.column,

        // Header
        header(
          display.flex,
          flexDirection.column,
          div(
            backgroundColor := backgroundSecondary,
            display.flex,
            flexDirection.column,
            justifyContent.center,
            height := "52px",
            width := "100%",
            color := "white",
            div(
              display.flex,
              padding := "0 40px",
              justifyContent.spaceBetween,
              div(
                display.flex,
                div(
                  i(cls := "fa-solid fa-clock", color := lightGray),
                  span(
                    "Пн-Вс 09:00-18:00"
                  )
                ),
                xA(
                  marginLeft := "5px",
                  i(cls := "fa-solid fa-download", color := lightGray),
                  span("Скачать прайс")
                )
              ),
              div(
                display.flex,
                xA("О компании"),
                xA(marginLeft := "10px", "Услуги"),
                xA(marginLeft := "10px", "Акции"),
                xA(marginLeft := "10px", "Доставка и оплата"),
                xA(marginLeft := "10px", "Контакты"),
                xA(marginLeft := "10px", "Отзывы")
              )
            )
          ),
          div(
            padding := "26px 0",
            div(
              padding := "0 20px",
              display.flex,
              flexDirection.row,
              alignItems.center,
              div(
                width := "145px",
                height := "52px",
                backgroundColor := "red",
                marginRight := "15px",
                "LOGO"
              ),
              button(
                display.flex,
                flexDirection.row,
                backgroundColor := buttonSecondary,
                padding := "14px",
                alignItems.center,
                border := "0px",
                borderRadius := "7px",
                i(
                  cls := "fa-solid fa-grip",
                  color := lightGray,
                  fontSize := "22px"
                ),
                span(
                  "Каталог",
                  color := white,
                  fontSize := "22px",
                  fontWeight := "700",
                  marginLeft := "10px"
                )
              ),
              button(
                display.flex,
                flexDirection.row,
                flexGrow := 1,
                justifyContent.spaceBetween,
                backgroundColor := white,
                padding := "12px",
                alignItems.center,
                border := s"2px solid $buttonSecondary",
                borderRadius := "7px",
                marginLeft := "10px",
                span(
                  "Искать в каталоге",
                  color := lightGray,
                  fontSize := "22px",
                  cursor.text
                ),
                i(
                  cls := "fa-solid fa-search",
                  color := buttonSecondary,
                  fontSize := "22px",
                  marginLeft := "5px"
                )
              ),
              div(
                display.flex,
                flexDirection.column,
                alignItems.center,
                justifyContent.spaceBetween,
                marginLeft := "10px",
                span(
                  fontSize := "26px",
                  fontWeight := "700",
                  "+7(918)371-10-01"
                ),
                xA("Перезвоните мне", color := primaryGray)
              )
            )
          ),
          div(
            display.flex,
            flexDirection.row,
            padding := "0 20px",
            div(
              display.flex,
              flexDirection.row,
              button(
                "Страны"
              ),
              button(
                "Бренды"
              )
            )
          )
        ),

        // Content
        inner
      )
    )
  )
