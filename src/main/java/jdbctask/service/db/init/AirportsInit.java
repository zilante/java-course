package jdbctask.service.db.init;

import jdbctask.domain.Airport;
import jdbctask.service.dao.AirportDao;
import jdbctask.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@AllArgsConstructor
public class AirportsInit {
    private final SimpleJdbcTemplate source;

    public void init(String fileName) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(fileName))) {
            AirportDao dao = new AirportDao(source);
            ArrayList<Airport> airports = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String airportName = values[1].split(":")[1].substring(3);
                airportName = airportName.substring(0, airportName.length() - 2);

                String city = values[3].split(":")[1].substring(3);
                city = city.substring(0, city.length() - 2);

                Airport airport = new Airport(values[0], airportName,
                        city, values[5] + values[6], values[7]);
                airports.add(airport);
            }

            dao.saveAirports(airports);
        }
    }
}
