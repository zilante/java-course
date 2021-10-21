import java.util.Scanner;

public class D {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            int personCount = in.nextInt();
            int offset = in.nextInt();

            System.out.println(getWinner(personCount, offset));
        }
    }

    private static int getWinner(int personCount, int offset) {
        int winner = 0;
        for (int i = 0; i < personCount; ++i) {
            winner = (winner + offset) % (i + 1);
        }

        return ++winner;
    }
}
