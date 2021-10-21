import java.util.Scanner;

public class A {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            int size = in.nextInt(); // size must be > 0

            if (size <= 0) {
                return;
            }

            int[] array1 = new int[size];
            readArray(in, array1);

            int[] array2 = new int[size];
            readArray(in, array2);

            int[] maxSumIndexes = new int[2];
            findMaxSumIndexes(array1, array2, maxSumIndexes);

            printArray(maxSumIndexes);
        }
    }

    private static void readArray(Scanner in, int[] array) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = in.nextInt();
        }
    }

    private static void printArray(int[] array) {
        for (int i = 0; i < array.length; ++i) {
            System.out.println(array[i]);
        }
    }

    private static void findMaxSumIndexes(int[] array1, int[] array2,
                                  int[] maxSumIndexes) {
        int maxSum = array1[0] + array2[0];
        int size = array1.length;
        int index1 = 0;
        int index2 = 0;
        int maxIndex1 = 0;

        for (int j = 1; j < size; ++j) {
            if (array1[maxIndex1] + array2[j] > maxSum) {
                index1 = maxIndex1;
                index2 = j;
                maxSum = array1[index1] + array2[index2];
            }

            if (array1[j] + array2[j] > maxSum) {
                index1 = j;
                index2 = j;
                maxSum = array1[index1] + array2[index2];
            }

            if (array1[j] > array1[maxIndex1]) {
                maxIndex1 = j;
            }
        }

        maxSumIndexes[0] = index1;
        maxSumIndexes[1] = index2;
    }
}
