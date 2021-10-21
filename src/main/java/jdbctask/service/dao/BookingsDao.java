package jdbctask.service.dao;

import jdbctask.service.db.SimpleJdbcTemplate;
import jdbctask.domain.Booking;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
public class BookingsDao {
    private final SimpleJdbcTemplate source;

    private Booking createBooking(ResultSet resultSet) throws SQLException {
        return new Booking(resultSet.getString("book_ref"), resultSet.getTimestamp("book_date"),
                resultSet.getDouble("total_amount"));
    }

    public void saveBookings(Collection<Booking> bookings) throws SQLException {
        source.preparedStatement(
                "insert into bookings(book_ref, book_date, total_amount)" +
                " values (?, ?, ?)", insertBooking -> {
            for (Booking booking : bookings) {
                insertBooking.setString(1, booking.getBookRef());
                insertBooking.setTimestamp(2, booking.getBookDate());
                insertBooking.setDouble(3, booking.getTotalAmount());
                insertBooking.execute();
            }
        });
    }

    public Set<Booking> getBookings() throws SQLException {
        return source.statement(stmt -> {
            Set<Booking> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from bookings");
            while (resultSet.next()) {
                result.add(createBooking(resultSet));
            }
            return result;
        });
    }
}


