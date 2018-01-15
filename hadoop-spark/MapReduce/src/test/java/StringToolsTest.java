import org.junit.Test;

/**
 * Created by wl on 10/24/17.
 */
public class StringToolsTest {

    @Test
    public void testDelimiter(){

//        String delimiter = "\t";
        String delimiter = "\1";

        String str = "|1|" + delimiter +  "|2|";

        String str2 = String.format("|1|\u0001|2|");

        System.out.println("==>" + str);
        System.out.println("==>" + str2);


    }

}
