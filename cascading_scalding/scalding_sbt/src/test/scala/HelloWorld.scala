/**
  * Created by wl on 1/5/18.
  */
object HelloWorld {
  def main(args: Array[String]): Unit = {
    println("Hello, world!")


    println(List(1,2,3).map(_ * 2))
    println(List(1,2,3).map{_ * 2})

    var val_list = List(1,2,3).map{x  => x * 2}
    println(val_list)
  }
}
