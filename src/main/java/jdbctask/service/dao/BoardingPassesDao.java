package jdbctask.service.dao;

import jdbctask.service.db.SimpleJdbcTemplate;
import jdbctask.domain.BoardingPass;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
public class BoardingPassesDao {
    private final SimpleJdbcTemplate source;

    private BoardingPass createBoardingPass(ResultSet resultSet) throws SQLException {
        return new BoardingPass(resultSet.getString("ticket_no"),
                resultSet.getInt("flight_id"),
                resultSet.getInt("boarding_no"), resultSet.getString("seat_no"));
    }

    public void saveBoardingPasses(Collection<BoardingPass> boardingPasses) throws SQLException {
        source.preparedStatement(
                "insert into boarding_passes(ticket_no, flight_id, boarding_no," +
                "seat_no)" +
                " values (?, ?, ?, ?)", insertBoardingPass -> {
            for (BoardingPass boardingPass : boardingPasses) {
                insertBoardingPass.setString(1, boardingPass.getTicketNo());
                insertBoardingPass.setInt(2, boardingPass.getFlightId());
                insertBoardingPass.setInt(3, boardingPass.getBoardingNo());
                insertBoardingPass.setString(4, boardingPass.getSeatNo());
                insertBoardingPass.execute();
            }
        });
    }

    public Set<BoardingPass> getBoardingPasses() throws SQLException {
        return source.statement(stmt -> {
            Set<BoardingPass> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from boarding_passes");
            while (resultSet.next()) {
                result.add(createBoardingPass(resultSet));
            }
            return result;
        });
    }

    public Integer getRowCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from boarding_passes");

            Integer count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            return count;
        });
    }

    public void deletePassesByAircraftModel(String model) throws SQLException {
        source.preparedStatement("delete from boarding_passes" +
                "  where flight_id in " +
                     " (select flight_id from flights" +
                      " where aircraft_code in" +
                         " (select aircraft_code from aircrafts" +
                          " where model = ?)" +
                     " )", deletePass -> {
            deletePass.setString(1, model);
            deletePass.execute();
        });
    }
}


