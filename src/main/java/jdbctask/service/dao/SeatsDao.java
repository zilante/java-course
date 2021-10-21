package jdbctask.service.dao;

import jdbctask.service.db.SimpleJdbcTemplate;
import jdbctask.domain.Seat;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
public class SeatsDao {
    private final SimpleJdbcTemplate source;

    private Seat createSeat(ResultSet resultSet) throws SQLException {
        return new Seat(resultSet.getString("aircraft_code"), resultSet.getString("seat_no"),
                resultSet.getString("fare_conditions"));
    }

    public void saveSeats(Collection<Seat> seats) throws SQLException {
        source.preparedStatement("insert into seats(aircraft_code, seat_no, fare_conditions)" +
                " values (?, ?, ?)", insertSeat -> {
            for (Seat seat : seats) {
                insertSeat.setString(1, seat.getAircraftCode());
                insertSeat.setString(2, seat.getSeatNo());
                insertSeat.setString(3, seat.getFareConditions());
                insertSeat.execute();
            }
        });
    }

    public Set<Seat> getSeats() throws SQLException {
        return source.statement(stmt -> {
            Set<Seat> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from seats");
            while (resultSet.next()) {
                result.add(createSeat(resultSet));
            }
            return result;
        });
    }
}

