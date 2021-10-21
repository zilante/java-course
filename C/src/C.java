import java.util.Scanner;

public class C {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            int size1 = in.nextInt(); // size must be > 0
            int[] array1 = new int[size1];
            readArray(in, array1);

            int size2 = in.nextInt(); // size must be > 0
            int[] array2 = new int[size2];
            readArray(in, array2);

            int necessarySum = in.nextInt();

            System.out.println(getNecessarySumCount(array1, array2,
                                                    necessarySum));
        }
    }

    private static void readArray(Scanner in, int[] array) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = in.nextInt();
        }
    }

    private static int getNecessarySumCount(int[] array1, int[] array2,
                                    int necessarySum) {
        int count = 0;

        int index1 = 0;
        int index2 = array2.length - 1;

        while (index1 < array1.length && index2 >= 0) {
            int currSum = array1[index1] + array2[index2];

            if (currSum > necessarySum) {
                --index2;
            } else if (currSum < necessarySum) {
                ++index1;
            } else {
                ++count;
                --index2;
                ++index1;
            }
        }

        return count;
    }
}
