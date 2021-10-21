package jdbctask.service.dao;

import jdbctask.service.db.SimpleJdbcTemplate;
import jdbctask.domain.Airport;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
public class AirportDao {
    private final SimpleJdbcTemplate source;

    private Airport createAirport(ResultSet resultSet) throws SQLException {
        return new Airport(resultSet.getString("airport_code"),
                resultSet.getString("airport_name"),
                resultSet.getString("city"), resultSet.getString("coordinates"),
                resultSet.getString("timezone"));
    }

    public void saveAirports(Collection<Airport> airports) throws SQLException {
        source.preparedStatement(
                "insert into airports(airport_code, airport_name, city," +
                "coordinates, timezone)" +
                " values (?, ?, ?, ?, ?)", insertAirport -> {
            for (Airport airport : airports) {
                insertAirport.setString(1, airport.getAirportCode());
                insertAirport.setString(2, airport.getAirportName());
                insertAirport.setString(3, airport.getCity());
                insertAirport.setString(4, airport.getCoordinates());
                insertAirport.setString(5, airport.getTimezone());
                insertAirport.execute();
            }
        });
    }

    public Set<Airport> getAirports() throws SQLException {
        return source.statement(stmt -> {
            Set<Airport> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from airports");
            while (resultSet.next()) {
                result.add(createAirport(resultSet));
            }
            return result;
        });
    }

    public Map<String, String> getCititesWithSeveralAirports() throws SQLException {
        return source.statement(stmt -> {
            Map<String, String> result = new HashMap<>();
            ResultSet resultSet = stmt.executeQuery("select city," +
                    " group_concat(airport_name) groupped_name" +
                    " from airports" +
                    " group by city" +
                    " having count(city) > 1");
            while (resultSet.next()) {
                result.put(resultSet.getNString(1), resultSet.getNString(2));
            }
            return result;
        });
    }
}

