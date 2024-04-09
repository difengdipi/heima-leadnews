import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: 周海
 * @Create : 2024/3/30
 **/
@SpringBootTest
public class test {
    @Test
    void ip() {
        generateMatrix(3);
    }

    public int[][] generateMatrix(int n) {
        int[][] result = new int[n][n];
        if (n == 1) {
            result[0][0] = 1;
            return result;
        }
        result[0][0] = 1;
        int index = 0;
        int row = 0, rank = 0;
        while (index < n) {
            if (row < n && rank == 0) {
                if (row == 0) {
                    row++;
                } else {
                    result[row][0] = result[row - 1][0] + 1;
                    row++;
                }
            } else if (row == n - 1) {
                result[row][rank] = result[row][rank - 1] + 1;
                rank++;
            } else if (row == 0 && rank == n - 1) {
                result[row][rank] = result[row - 1][rank] + 1;
                rank--;
            } else if (row == n - 1 && rank == n - 1) {
                result[row][rank] = result[row][rank - 1] + 1;
                row--;
            }
            index++;
        }
        return result;
    }
}
