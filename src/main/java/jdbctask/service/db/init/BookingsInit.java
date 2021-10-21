package jdbctask.service.db.init;

import jdbctask.domain.Booking;
import jdbctask.service.dao.BookingsDao;
import jdbctask.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

@AllArgsConstructor
public class BookingsInit {
    private final SimpleJdbcTemplate source;

    public void init(String fileName) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(fileName))) {
            BookingsDao dao = new BookingsDao(source);
            ArrayList<Booking> bookings = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Booking booking = new Booking(values[0],
                        Timestamp.valueOf(values[1].substring(0, 19)),
                        Double.parseDouble(values[2]));
                bookings.add(booking);
            }

            dao.saveBookings(bookings);
        }
    }
}

