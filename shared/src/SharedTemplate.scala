import scalatags.generic.Bundle

class SharedTemplate[Builder, Output <: FragT, FragT](bundler: Bundle[Builder, Output, FragT]) {
  import bundler.all.* 

  def renderHello(state: Int) = 
    h1(s"Hello world - $state")
}
