package jdbctask.service.dao;

import jdbctask.service.db.SimpleJdbcTemplate;
import jdbctask.domain.TicketFlight;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
public class TicketFlightsDao {
    private final SimpleJdbcTemplate source;

    private TicketFlight createTicketFlight(ResultSet resultSet) throws SQLException {
        return new TicketFlight(resultSet.getString("ticket_no"), resultSet.getInt("flight_id"),
                resultSet.getString("fare_conditions"), resultSet.getDouble("amount"));
    }

    public void saveTicketFlights(Collection<TicketFlight> ticketFlights) throws SQLException {
        source.preparedStatement("insert into ticket_flights(ticket_no," +
                " flight_id, fare_conditions, amount)" +
                " values (?, ?, ?, ?)", insertTicketFlight -> {
            for (TicketFlight ticketFlight : ticketFlights) {
                insertTicketFlight.setString(1, ticketFlight.getTicketNo());
                insertTicketFlight.setInt(2, ticketFlight.getFlightId());
                insertTicketFlight.setString(3, ticketFlight.getFareConditions());
                insertTicketFlight.setDouble(4, ticketFlight.getAmount());
                insertTicketFlight.execute();
            }
        });
    }

    public Set<TicketFlight> getTicketFlights() throws SQLException {
        return source.statement(stmt -> {
            Set<TicketFlight> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from ticket_flights");
            while (resultSet.next()) {
                result.add(createTicketFlight(resultSet));
            }
            return result;
        });
    }

    public Integer getRowCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from ticket_flights");

            Integer count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            return count;
        });
    }

    public void deleteTicketFlightsByAircraftModel(String model) throws SQLException {
        source.preparedStatement("delete from ticket_flights" +
                "  where flight_id in " +
                     " (select flight_id from flights" +
                      " where aircraft_code in" +
                          " (select aircraft_code from aircrafts" +
                           " where model = ?)" +
                     " )", deleteTicketFlight -> {
            deleteTicketFlight.setString(1, model);
            deleteTicketFlight.execute();
        });
    }
}


