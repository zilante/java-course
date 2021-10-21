package jdbctask.service.db.init;

import jdbctask.domain.Aircraft;
import jdbctask.service.dao.AircraftDao;
import jdbctask.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@AllArgsConstructor
public class AircraftsInit {
    private final SimpleJdbcTemplate source;

    public void init(String fileName) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(fileName))) {
            AircraftDao dao = new AircraftDao(source);
            ArrayList<Aircraft> aircrafts = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String model = values[1].split(":")[1].substring(3);
                model = model.substring(0, model.length() - 2);

                Aircraft aircraft = new Aircraft(values[0], model,
                        Integer.parseInt(values[3]));
                aircrafts.add(aircraft);
            }

           dao.saveAircrafts(aircrafts);
        }
    }
}
