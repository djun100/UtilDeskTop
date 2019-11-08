import com.cy.data.UtilString;
import org.junit.Test;

import java.util.Arrays;

public class UStringTest {
    @Test
    public void test() {//-7//-7
        System.out.println(UtilString.repeat("哈哈", 5));

        int arr[] = new int[]{1, 3, 4, 5, 8, 9};
        Arrays.sort(arr);
        //1, 3, 4, 5, (6), 8, 9   //-5
        int index1 = Arrays.binarySearch(arr, 6);
        //1, 3, _4_, 5, 8, 9  //2
        int index2 = Arrays.binarySearch(arr, 4);
        //(0), 1, 3, 4, 5, 8, 9 //-1
        int index3 = Arrays.binarySearch(arr, 0);
        //1, 3, 4, 5, 8, 9, (10) //-7
        int index4 = Arrays.binarySearch(arr, 10);

        System.out.println("index1 = " + index1 + ", index2 = " + index2 +
                ", index3 = " + index3 + ", index4 = " + index4);

    }
}
