package checkers;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldTest {
    @BeforeClass
    public static void start(){
        System.out.println("Testing started");
    }

    @AfterClass
    public static void finish(){
        System.out.println("Testing finished");
    }

    @Test
    public void isNeighbour() {
        String position1 = "D4";
        String position2 = "e5";

        Field field1 = new Field(position1, Colour.BLACK);
        Field field2 = new Field(position2, Colour.WHITE);

        boolean actual = field1.isNeighbour(field2);
        boolean expected = true;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void isNotNeighbour() {
        String position1 = "D4";
        String position2 = "a7";

        Field field1 = new Field(position1, Colour.BLACK);
        Field field2 = new Field(position2, Colour.WHITE);

        boolean actual = field1.isNeighbour(field2);
        boolean expected = false;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void isOpponent() {
        String position1 = "D4";
        String position2 = "a7";

        Field field1 = new Field(position1, Colour.BLACK);
        Field field2 = new Field(position2, Colour.WHITE);

        boolean actual = field1.isOpponent(field2);
        boolean expected = true;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void isNotOpponent() {
        String position1 = "D4";
        String position2 = "a7";

        Field field1 = new Field(position1, Colour.BLACK);
        Field field2 = new Field(position2, Colour.BLACK);

        boolean actual = field1.isOpponent(field2);
        boolean expected = false;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void isSameDiagonal() {
        String position1 = "D4";
        String position2 = "a7";

        Field field1 = new Field(position1, Colour.BLACK);
        Field field2 = new Field(position2, Colour.BLACK);

        boolean actual = field1.isSameDiagonal(field2);
        boolean expected = true;

        Assert.assertEquals(expected, actual);
    }
}