package shared

import scalatags.generic.Bundle

class SharedTemplate[Builder, Output <: FragT, FragT](bundler: Bundle[Builder, Output, FragT]) {
  import bundler.all.* 

  // TODO: inject dispatch in a staticly typed way
  def renderHello(state: Int) = 
    div(
        display.flex,
        flexDirection.column,
        h1(
            s"Hello world - $state",
        ),
        button(
            s"Increment",
            onclick := "dispatch('INCREMENT')"
        ),
        button(
            s"Decrement",
            onclick := "dispatch('DECREMENT')"
        ),
        button(
            s"Print",
            onclick := "dispatch('PRINT')"
        )
    )

  // TODO: 
  // def headerLayout (componentState, globalState/context, globalFunctions (locale("en"), theme("light"))) = {
  //   div(
  //     backgroundColor := theme(globalState.theme).backgroundColor
  //   )
  // }

  // TODO: intercomponent communication
  // TODO: async actions - state concurency
}
