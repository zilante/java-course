package jdbctask.service.dao;

import jdbctask.service.db.SimpleJdbcTemplate;
import jdbctask.domain.Flight;
import lombok.AllArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@AllArgsConstructor
public class FlightsDao {
    private final SimpleJdbcTemplate source;

    private Flight createFlight(ResultSet resultSet) throws SQLException {
        return new Flight(resultSet.getInt("flight_id"), resultSet.getString("flight_no"),
                resultSet.getTimestamp("scheduled_departure"),
                resultSet.getTimestamp("scheduled_arrival"),
                resultSet.getString("departure_airport"),
                resultSet.getString("arrival_airport"),
                resultSet.getString("status"), resultSet.getString("aircraft_code"),
                resultSet.getTimestamp("actual_departure"),
                resultSet.getTimestamp("actual_arrival"));
    }

    public void saveFlights(Collection<Flight> flights) throws SQLException {
        source.preparedStatement(
                "insert into flights(flight_id, flight_no, scheduled_departure," +
                "scheduled_arrival, departure_airport, arrival_airport, status," +
                "aircraft_code, actual_departure, actual_arrival)" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", insertFlight -> {
            for (Flight flight : flights) {
                insertFlight.setInt(1, flight.getFlightId());
                insertFlight.setString(2, flight.getFlightNo());
                insertFlight.setTimestamp(3, flight.getScheduledDeparture());
                insertFlight.setTimestamp(4, flight.getScheduledArrival());
                insertFlight.setString(5, flight.getDepartureAirport());
                insertFlight.setString(6, flight.getArrivalAirport());
                insertFlight.setString(7, flight.getStatus());
                insertFlight.setString(8, flight.getAircraftCode());
                insertFlight.setTimestamp(9, flight.getActualDeparture());
                insertFlight.setTimestamp(10, flight.getActualArrival());
                insertFlight.execute();
            }
        });
    }

    public Set<Flight> getFlights() throws SQLException {
        return source.statement(stmt -> {
            Set<Flight> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from flights");
            while (resultSet.next()) {
                result.add(createFlight(resultSet));
            }
            return result;
        });
    }

    public Integer getRowCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from flights");

            Integer count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            return count;
        });
    }

    public Map<String, Integer> getCitiesWithMostOftenFlightCancels() throws SQLException {
        return source.statement(stmt -> {
            Map<String, Integer> result = new HashMap<>();
            ResultSet resultSet = stmt.executeQuery("select city, count(city) as cancel_count" +
                    " from airports inner join (select departure_airport" +
                                         " from flights" +
                                         " where status = 'Cancelled') canceled_flights" +
                    " on airports.airport_code = canceled_flights.departure_airport" +
                    " group by city" +
                    " order by cancel_count desc" +
                    " limit 10");
            while (resultSet.next()) {
                result.put(resultSet.getNString(1), Integer.parseInt(resultSet.getNString(2)));
            }
            return result;
        });
    }

    public List<List> getShortestFlightsInCities() throws SQLException {
        return source.statement(stmt -> {
            List<List> result = new ArrayList<>();

            List<String> fromCities = new ArrayList<>();
            List<String> toCities = new ArrayList<>();
            List<Double> avgDurations = new ArrayList<>();

            String flightDurationsWithCitiesSql =
                    "(select from_city, airports.city as arrival_city, duration" +
                    " from airports inner join" +
                         " (select city as from_city," +
                                 " arrival_airport, duration" +
                          " from airports inner join" +
                               " (select departure_airport," +
                                       " arrival_airport," +
                                       " timestampdiff(minute," +
                             " actual_departure, actual_arrival) as duration" +
                                " from flights" +
                                " where actual_arrival is not null" +
                                ") flight_durations" +
                          " on airports.airport_code = flight_durations.departure_airport" +
                         ") flight_durations_with_from_city" +
                    " on airports.airport_code = flight_durations_with_from_city.arrival_airport" +
                    ") flight_durations_with_cities";

            ResultSet resultSet = stmt.executeQuery(
            "select distinct groupby_from_city.from_city, arrival_city, avg_dur" +
              " from (select from_city, min(duration) as min_dur, avg(duration) as avg_dur" +
                    " from " + flightDurationsWithCitiesSql +
                    " group by from_city" +
                    " order by min_dur" +
                    ") groupby_from_city" +
                    " inner join " + flightDurationsWithCitiesSql +
                    " on min_dur = flight_durations_with_cities.duration and" +
                       " groupby_from_city.from_city = flight_durations_with_cities.from_city"
            );

            while (resultSet.next()) {
                fromCities.add(resultSet.getNString(1));
                toCities.add(resultSet.getNString(2));
                avgDurations.add(Double.parseDouble(resultSet.getNString(3)));
            }

            result.add(fromCities);
            result.add(toCities);
            result.add(avgDurations);
            return result;
        });
    }

    public Map<Integer, Integer> getCancelCountByMonth() throws SQLException {
        return source.statement(stmt -> {
            Map<Integer, Integer> result = new HashMap<>();
            ResultSet resultSet = stmt.executeQuery(
                    "select flight_month, count(flight_month)" +
                    " from (select extract(MONTH FROM scheduled_departure) as flight_month" +
                            " from flights" +
                            " where status = 'Cancelled')" +
                    " group by flight_month");

            while (resultSet.next()) {
                result.put(Integer.parseInt(resultSet.getNString(1)),
                        Integer.parseInt(resultSet.getNString(2)));
            }

            return result;
        });
    }

    public Map<Integer, Integer> getFlightCountByWeekdayFromCity(String city)
            throws SQLException {
        Map<Integer, Integer> flightCountByWeekday = new HashMap<>();

        source.preparedStatement(
                "select week_day, count(week_day)" +
                    " from (select dayofweek(actual_departure) as week_day" +
                           " from flights" +
                           " where departure_airport in" +
                                  " (select airport_code from" +
                                   " airports where city = ?" +
                                   ")" +
                                   " and actual_arrival is not null" +
                           ")" +
                    " group by week_day" +
                    " order by week_day",
                selectFlightCount -> {
                    selectFlightCount.setString(1, city);

                    ResultSet resultSet = selectFlightCount.executeQuery();

                    while (resultSet.next()) {
                        flightCountByWeekday.put(
                                Integer.parseInt(resultSet.getNString(1)),
                                Integer.parseInt(resultSet.getNString(2)));
                    }
        });

        return flightCountByWeekday;
    }

    public Map<Integer, Integer> getFlightCountByWeekdayToCity(String city)
            throws SQLException {
        Map<Integer, Integer> flightCountByWeekday = new HashMap<>();

        source.preparedStatement(
                "select week_day, count(week_day)" +
                        " from (select dayofweek(actual_departure) as week_day" +
                        " from flights" +
                        " where arrival_airport in" +
                        " (select airport_code from" +
                        " airports where city = ?" +
                        ")" +
                        " and actual_arrival is not null" +
                        ")" +
                        " group by week_day" +
                        " order by week_day",
                selectFlightCount -> {
                    selectFlightCount.setString(1, city);

                    ResultSet resultSet = selectFlightCount.executeQuery();

                    while (resultSet.next()) {
                        flightCountByWeekday.put(
                                Integer.parseInt(resultSet.getNString(1)),
                                Integer.parseInt(resultSet.getNString(2)));
                    }
                });

        return flightCountByWeekday;
    }

    public void deleteFlightsByAircraftModel(String model) throws SQLException {
        source.preparedStatement("delete from flights" +
                " where aircraft_code in" +
                " (select aircraft_code from aircrafts" +
                "  where model = ?" +
                " )", deleteFlight -> {
                deleteFlight.setString(1, model);
                deleteFlight.execute();
        });
    }

    public Map<String, Double> getLossesAfterCancellingFlightsBetween(
            String after, String before) throws SQLException {
        List<String> cancelQueries = new ArrayList<>();
        List<SimpleJdbcTemplate.SQLConsumer<? super PreparedStatement>> cancelConsumers =
                new ArrayList<>();

        String updateFlightsSql = "update flights" +
                " set status = 'Cancelled'" +
                " where scheduled_departure" +
                " between ? and ?";
        cancelQueries.add(updateFlightsSql);

        String getLossSql = "select sched_departure_date, sum(amount)" +
                            " from ticket_flights" +
                            " inner join" +
                                " (select flight_id," +
                                        " formatdatetime(scheduled_departure,'dd')" +
                                        " as sched_departure_date" +
                                "  from flights" +
                                 " where scheduled_departure" +
                                 " between ? and ?) to_cancel_flights" +
                            " on ticket_flights.flight_id = to_cancel_flights.flight_id" +
                            " group by sched_departure_date";

        cancelConsumers.add(updateFlights -> {
            updateFlights.setString(1, after);
            updateFlights.setString(2, before);
            updateFlights.execute();
        });

        return source.preparedTransaction(cancelQueries, getLossSql, cancelConsumers,
                selectSumAmount -> {
                    selectSumAmount.setString(1, after);
                    selectSumAmount.setString(2, before);

                    Map<String, Double> lossByDate = new HashMap<>();
                    ResultSet resultSet = selectSumAmount.executeQuery();

                    while (resultSet.next()) {
                        String date = resultSet.getNString(1);
                        Double loss = Double.parseDouble(resultSet.getNString(2));

                        lossByDate.put(date, loss);
                    }

                    return lossByDate;
                });
    }
}
