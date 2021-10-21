package jdbctask.service.dao;

import jdbctask.service.db.SimpleJdbcTemplate;
import jdbctask.domain.Ticket;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
public class TicketsDao {
    private final SimpleJdbcTemplate source;

    private Ticket createTicket(ResultSet resultSet) throws SQLException {
        return new Ticket(resultSet.getString("ticket_no"),
                resultSet.getString("book_ref"),
                resultSet.getString("passenger_id"), resultSet.getString("passenger_name"),
                resultSet.getString("contact_data"));
    }

    public void saveTickets(Collection<Ticket> tickets) throws SQLException {
        source.preparedStatement("insert into tickets(ticket_no," +
                "book_ref, passenger_id, passenger_name, contact_data)" +
                " values (?, ?, ?, ?, ?)", insertTicket -> {
            for (Ticket ticket : tickets) {
                insertTicket.setString(1, ticket.getTicketNo());
                insertTicket.setString(2, ticket.getBookRef());
                insertTicket.setString(3, ticket.getPassengerId());
                insertTicket.setString(4, ticket.getPassengerName());
                insertTicket.setString(5, ticket.getContactData());
                insertTicket.execute();
            }
        });
    }

    public Set<Ticket> getTickets() throws SQLException {
        return source.statement(stmt -> {
            Set<Ticket> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from tickets");
            while (resultSet.next()) {
                result.add(createTicket(resultSet));
            }
            return result;
        });
    }

    public Integer getRowCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from tickets");

            Integer count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            return count;
        });
    }

    public void deleteTicketsByAircraftModel(String model) throws SQLException {
        source.preparedStatement("delete from tickets" +
                " where ticket_no in" +
                " (select ticket_no from ticket_flights" +
                "  where flight_id in " +
                      " (select flight_id from flights" +
                       " where aircraft_code in" +
                            " (select aircraft_code from aircrafts" +
                             " where model = ?)" +
                      " )" +
                  ")", deleteTicket -> {
            deleteTicket.setString(1, model);
            deleteTicket.execute();
        });
    }
}


