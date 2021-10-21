package checkers;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            String whitePositionsString = in.nextLine();
            String blackPositionsString = in.nextLine();
            CheckersConfiguration configuration =
                    new CheckersConfiguration(whitePositionsString,
                            blackPositionsString);

            while (in.hasNextLine()){
                String moves = in.nextLine();

                try {
                    configuration.makeMoves(moves);
                } catch(Exception exception) {
                    System.out.println(exception.getMessage());
                    return;
                }
            }

            System.out.println(configuration.getAllPieces());
        }
    }
}