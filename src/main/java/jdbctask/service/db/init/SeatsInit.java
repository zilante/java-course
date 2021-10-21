package jdbctask.service.db.init;

import jdbctask.domain.Seat;
import jdbctask.service.dao.SeatsDao;
import jdbctask.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@AllArgsConstructor
public class SeatsInit {
    private final SimpleJdbcTemplate source;

    public void init(String fileName) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(fileName))) {
            SeatsDao dao = new SeatsDao(source);
            ArrayList<Seat> seats = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Seat seat = new Seat(values[0], values[1], values[2]);
                seats.add(seat);
            }

            dao.saveSeats(seats);
        }
    }
}

