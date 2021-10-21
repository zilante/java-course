package checkers;

import org.junit.*;

import static org.junit.Assert.*;

public class CheckersConfigurationTest {
    @BeforeClass
    public static void start(){
        System.out.println("Testing started");
    }

    @AfterClass
    public static void finish(){
        System.out.println("Testing finished");
    }

    @Test
    public void getAllPieces() {
        String whites_configuration = "a1 a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2";
        String blacks_configuration = "a7 b6 b8 c7 d6 d8 e7 f6 f8 g7 h6 h8";

        CheckersConfiguration configuration = new CheckersConfiguration(
                whites_configuration, blacks_configuration);

        String expected = whites_configuration + "\n" + blacks_configuration;
        String actual = configuration.getAllPieces();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void makeMoves() {
        String whites_configuration = "a1 a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2";
        String blacks_configuration = "a7 b6 b8 c7 d6 d8 e7 f6 f8 g7 h6 h8";

        CheckersConfiguration configuration = new CheckersConfiguration(
                whites_configuration, blacks_configuration);

        String moves = "g3-f4 f6-e5";

        try {
            configuration.makeMoves(moves);
        } catch(Exception exception) {

        }

        String actual = configuration.getAllPieces();
        String expected = "a1 a3 b2 c1 c3 d2 e1 e3 f2 f4 g1 h2\n" +
                "a7 b6 b8 c7 d6 d8 e5 e7 f8 g7 h6 h8";

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = InvalidMoveException.class)
    public void makeInvalidMove() throws InvalidMoveException,
            CheckersErrorException {
        String whites_configuration = "D8 c5 f6 g1";
        String blacks_configuration = "a5 f4 h6 h8";

        CheckersConfiguration configuration = new CheckersConfiguration(
                whites_configuration, blacks_configuration);

        String move1 = "c5-d6 f4-e3";
        String move2 = "d6-c7 h8-g7";
        String move3 = "c7-b8 g7:e5";
        String move4 = "B8:F4:D2 h6-g5";

        configuration.makeMoves(move1);
        configuration.makeMoves(move2);
        configuration.makeMoves(move3);
        configuration.makeMoves(move4);
    }

    @Test
    public void getAllPiecesWithKingsFirst() {
        String whites_configuration = "a5";
        String blacks_configuration = "a7 b8 C7 d8";

        CheckersConfiguration configuration = new CheckersConfiguration(
                whites_configuration, blacks_configuration);

        String expected = whites_configuration + "\n" + "C7 a7 b8 d8";
        String actual = configuration.getAllPieces();

        Assert.assertEquals(expected, actual);
    }
}