package jdbctask.service.db.init;

import jdbctask.domain.Ticket;
import jdbctask.service.dao.TicketsDao;
import jdbctask.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@AllArgsConstructor
public class TicketsInit {
    private final SimpleJdbcTemplate source;

    public void init(String fileName) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(fileName))) {
            TicketsDao dao = new TicketsDao(source);
            ArrayList<Ticket> tickets = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String contactData;
                if (values.length == 5) {
                    contactData = values[4];
                } else {
                    contactData = null;
                }

                Ticket ticket = new Ticket(values[0],
                        values[1], values[2],
                        values[3], contactData);
                tickets.add(ticket);
            }

            dao.saveTickets(tickets);
        }
    }
}

