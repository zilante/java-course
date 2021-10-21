package jdbctask.service.db.init;

import jdbctask.domain.Flight;
import jdbctask.service.dao.FlightsDao;
import jdbctask.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

@AllArgsConstructor
public class FlightsInit {
    private final SimpleJdbcTemplate source;

    public void init(String fileName) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(fileName))) {
            FlightsDao dao = new FlightsDao(source);
            ArrayList<Flight> flights = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                Timestamp actualDeparture;
                Timestamp actualArrival;
                if (values.length == 10) {
                    actualDeparture = Timestamp.valueOf(values[8].substring(0, 19));
                    actualArrival = Timestamp.valueOf(values[9].substring(0, 19));
                } else if (line.length() == 9){
                    actualDeparture = Timestamp.valueOf(values[8].substring(0, 19));
                    actualArrival = null;
                } else {
                    actualDeparture = null;
                    actualArrival = null;
                }

                Flight flight = new Flight(Integer.parseInt(values[0]),
                        values[1],
                        Timestamp.valueOf(values[2].substring(0, 19)),
                        Timestamp.valueOf(values[3].substring(0, 19)),
                        values[4], values[5], values[6], values[7],
                        actualDeparture,
                        actualArrival);

                flights.add(flight);
            }

            dao.saveFlights(flights);
        }
    }
}