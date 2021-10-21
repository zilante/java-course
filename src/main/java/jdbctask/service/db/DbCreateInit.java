package jdbctask.service.db;

import jdbctask.Hello;
import jdbctask.service.db.init.AircraftsInit;
import jdbctask.service.db.init.AirportsInit;
import jdbctask.service.db.init.BoardingPassesInit;
import jdbctask.service.db.init.BookingsInit;
import jdbctask.service.db.init.FlightsInit;
import jdbctask.service.db.init.SeatsInit;
import jdbctask.service.db.init.TicketFlightsInit;
import jdbctask.service.db.init.TicketsInit;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * Creates and initializes database
 */
@AllArgsConstructor
public class DbCreateInit {
    final SimpleJdbcTemplate source;

    private String getSQL(String name) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        Hello.class.getResourceAsStream(name),
                        StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    public void create() throws SQLException, IOException {
        String sql = getSQL("dbcreate.sql");
        source.statement(stmt -> {
            stmt.execute(sql);
        });
    }

    public void init() throws IOException, SQLException {
        String dbFilesDirPath = "./src/main/resources/jdbctask/db/";

        new AircraftsInit(source).init(dbFilesDirPath + "aircrafts_data.csv");
        new AirportsInit(source).init(dbFilesDirPath + "airports_data.csv");
        new BoardingPassesInit(source).init(dbFilesDirPath + "boarding_passes.csv");
        new BookingsInit(source).init(dbFilesDirPath + "bookings.csv");
        new FlightsInit(source).init(dbFilesDirPath + "flights.csv");
        new SeatsInit(source).init(dbFilesDirPath + "seats.csv");
        new TicketFlightsInit(source).init(dbFilesDirPath + "ticket_flights.csv");
        new TicketsInit(source).init(dbFilesDirPath + "tickets.csv");
    }
}

