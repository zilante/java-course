import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class B {
    public static void main(String[] args) {
        try {
            System.out.println(getInputPolygonSquare());
        } catch (IOException exception) {
            System.out.println(exception.toString());
        }
    }

    private static int[] getInputIntegersLine(BufferedReader br)
            throws IOException {
        String[] integersString;
        int[] integers;

        integersString = br.readLine().split(" ");
        integers = new int[integersString.length];

        for (int i = 0; i < integers.length; ++i) {
            integers[i] = Integer.parseInt(integersString[i]);
        }

        return integers;
    }

    private static double getInputPolygonSquare() throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(System.in))) {
            int vertexCount = getInputIntegersLine(br)[0];

            if (vertexCount <= 0) {
                return 0;
            }

            int doubledSquare = 0;

            // start point
            int[] point0 = getInputIntegersLine(br);
            int x0 = point0[0];
            int y0 = point0[1];

            int x1 = x0;
            int y1 = y0;
            for (int i = 1; i < vertexCount; ++i) {
                int[] point = getInputIntegersLine(br);
                int x2 = point[0];
                int y2 = point[1];

                doubledSquare += (x2 * y1 - x1 * y2); // doubled triangle square

                x1 = x2;
                y1 = y2;
            }
            doubledSquare += (x0 * y1 - x1 * y0);

            return (double) (doubledSquare) / 2.0;
        }
    }
}
