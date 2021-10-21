package jdbctask.service.db.init;

import jdbctask.domain.BoardingPass;
import jdbctask.service.dao.BoardingPassesDao;
import jdbctask.service.db.SimpleJdbcTemplate;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@AllArgsConstructor
public class BoardingPassesInit {
    private final SimpleJdbcTemplate source;

    public void init(String fileName) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(
                new FileReader(fileName))) {
            BoardingPassesDao dao = new BoardingPassesDao(source);
            ArrayList<BoardingPass> boardingPasses = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                BoardingPass boardingPass = new BoardingPass(values[0],
                        Integer.parseInt(values[1]), Integer.parseInt(values[2]),
                        values[3]);
                boardingPasses.add(boardingPass);
            }

            dao.saveBoardingPasses(boardingPasses);
        }
    }
}

