import java.sql._

/**
  * Created by wl on 17-6-17.
  */
object PGTest extends App {


  println("Postgres connector")

//  classOf[org.postgresql.Driver]
  Class.forName("org.postgresql.Driver").newInstance
  val con_str = "jdbc:postgresql://localhost:5432/ipinbase?user=wanglong&password=wanglong"
  val conn = DriverManager.getConnection(con_str)
  try {
    val stm = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

    val rs = stm.executeQuery("SELECT * from public.student1")

    while(rs.next) {
      println(rs.getString("name"))
    }
    rs.close()
    stm.close()
  } finally {
    conn.close()
  }

}
