package library

import scala.collection.mutable
import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom
import org.scalajs.dom.Element
import org.scalajs.dom.Node
import shared.SharedTemplate
import scalatags.generic.TypedTag

trait Component[State, Action] (
    val template: (State) => TypedTag[Element, Element, Node],
) {
    def reduce (state: State, action: Action): State
}

case class RuntimeComponent[State, Action] (
    containerId: String,
    component: Component[State, Action],
    state: State,
    stringToAction: String => Action
)

object Core {
    // TODO: each action stores its own runtime component, fix
    val store: mutable.Map[String, RuntimeComponent[?, ?]] = mutable.Map.empty

    @JSExportTopLevel("dispatch")
    def dispatch (actionName: String) = {
        store.get(actionName) match {
            case Some(runtimeComponent) => {
                val container = dom.document.getElementById(runtimeComponent.containerId)

                val newState = runtimeComponent.component.reduce(
                    runtimeComponent.state,
                    runtimeComponent.stringToAction(actionName)
                )

                val newContent = runtimeComponent.component.template(newState).render

                container.innerHTML = ""
                container.appendChild(newContent)

                store.update(actionName, runtimeComponent.copy(state = newState))
            }
            case None => println(s"No component found to handle dispatched action. Action: $actionName")
        }
    }

    def register[State, Action] (
        containerId: String,
        component: Component[State, Action],
        initialState: State,
        actions: Set[Action],
        actionToString: Action => String,
        stringToAction: String => Action
    ) = {
        actions.foreach((action) => {
            store.update(
                actionToString(action),
                RuntimeComponent(
                    containerId,
                    component,
                    initialState,
                    stringToAction
                )
            )
        })
    }
}
