/**
  * Created by wl on 11/7/17.
  */
object UnderscoreTest {

  def main(args: Array[String]): Unit = {
    val nums = List(1,2,3,4,5,6,7,8,9,10)

    println(nums.filter (_ % 2 == 0))

    println(nums.reduce (_ + _))

    println(nums.exists(_ > 5))

    println(nums.takeWhile(_ < 8))
  }
}
