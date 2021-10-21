package jdbctask.service.db.init;

import jdbctask.domain.TicketFlight;
import jdbctask.service.dao.TicketFlightsDao;
import jdbctask.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@AllArgsConstructor
public class TicketFlightsInit {
    private final SimpleJdbcTemplate source;

    public void init(String fileName) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(fileName))) {
            TicketFlightsDao dao = new TicketFlightsDao(source);
            ArrayList<TicketFlight> ticketFlights = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                TicketFlight ticketFlight = new TicketFlight(values[0],
                        Integer.parseInt(values[1]), values[2],
                        Double.parseDouble(values[3]));
                ticketFlights.add(ticketFlight);
            }

            dao.saveTicketFlights(ticketFlights);
        }
    }
}

