import jdbctask.reports.excel.*;
import jdbctask.reports.chart.*;
import jdbctask.service.dao.*;
import jdbctask.service.db.SimpleJdbcTemplate;
import jdbctask.service.db.DbCreateInit;
import jdbctask.domain.Airport;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

class QueriesTest {
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(
            JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1", "", ""));
    private AirportDao airportDao = new AirportDao(source);
    private FlightsDao flightsDao = new FlightsDao(source);
    private TicketsDao ticketsDao = new TicketsDao(source);
    private TicketFlightsDao ticketFlightsDao = new TicketFlightsDao(source);
    private BoardingPassesDao boardingPassesDao = new BoardingPassesDao(source);

    @BeforeAll
    public static void setupDB() throws IOException, SQLException {
        DbCreateInit dbCreateInit = new DbCreateInit(source);

        dbCreateInit.create();
        dbCreateInit.init();
    }

    @AfterAll
    public static void tearDownDB() throws SQLException, IOException {
        source.statement(stmt -> {
            stmt.execute("drop all objects;");
        });
    }

    @Test
    void getCitiesWithSeveralAirports() throws IOException, SQLException {
        Set<Airport> airports = airportDao.getAirports();
        System.out.println("in test airports:");
        System.out.println(airports);

        Map<String, String> cititesWithSeveralAirports =
                airportDao.getCititesWithSeveralAirports();

        new CitiesWithSeveralAirports()
                .writeInExcelFile(
                        "./src/main/reports/excel/cities-with-several-airports.xlsx",
                        cititesWithSeveralAirports);

        System.out.println("in test cities with several airports:");
        System.out.println(cititesWithSeveralAirports);
    }

    @Test
    void getCitiesWithMostOftenFlightCancels() throws SQLException, IOException {
        Map<String, Integer> citiesWithMostOftenFlightCancels =
                flightsDao.getCitiesWithMostOftenFlightCancels();

        new CitiesWithMostOftenFlightCancels()
                .writeInExcelFile(
                        "./src/main/reports/excel/cities-with-most-often-flight-cancels.xlsx",
                        citiesWithMostOftenFlightCancels);

        System.out.println("in test cities with most often cancels:");
        System.out.println(citiesWithMostOftenFlightCancels);
    }

    @Test
    void getShortestFlightsInCities() throws SQLException, IOException {
        List<List> shortestFlightsInCities = flightsDao.getShortestFlightsInCities();

        new ShortestFlightsInCities()
                .writeInExcelFile(
                        "./src/main/reports/excel/shortest-flights-in-cities.xlsx",
                        shortestFlightsInCities);

        System.out.println("in test shortest flights from cities:");
        System.out.println(shortestFlightsInCities.get(0));

        System.out.println("in test shortest flights to cities:");
        System.out.println(shortestFlightsInCities.get(1));

        System.out.println("in test shortest flights avg dur:");
        System.out.println(shortestFlightsInCities.get(2));
    }

    @Test
    void getCancelCountByMonth() throws SQLException, IOException {
        Map<Integer, Integer> cancelCountByMonth = flightsDao.getCancelCountByMonth();

        new CancelCountByMonth()
                .writeInExcelFile(
                        "./src/main/reports/excel/cancel-count-by-month.xlsx",
                        cancelCountByMonth);

        new CancelCountByMonthChart()
                .drawChart(
                        "./src/main/reports/charts/cancel-count-by-month.png",
                        cancelCountByMonth);

        System.out.println("in test cancel count by month:");
        System.out.println(cancelCountByMonth);
    }

    @Test
    void getFlightCountByWeekdayFromToCity() throws SQLException, IOException {
        String moscow = "Moscow";

        Map<Integer, Integer> flightCountFromByWeekday =
                flightsDao.getFlightCountByWeekdayFromCity(moscow);
        Map<Integer, Integer> flightCountToByWeekday =
                flightsDao.getFlightCountByWeekdayToCity(moscow);

        new FlightCountByWeekdayFromToMoscow()
                .writeInExcelFile(
                        "./src/main/reports/excel/flight-count-from-to-moscow.xlsx",
                        flightCountFromByWeekday, flightCountToByWeekday);

        new FlightCountByWeekdayFromToMoscowChart()
                .drawChart(
                        "./src/main/reports/charts/flight-count-by-weekday-from-moscow.png",
                        "./src/main/reports/charts/flight-count-by-weekday-to-moscow.png",
                        flightCountFromByWeekday, flightCountToByWeekday
                        );

        System.out.println("in test flight count by weekday from Moscow:");
        System.out.println(flightCountFromByWeekday);
    }

    @Test
    void getFlightCountByWeekdayToCity() throws SQLException {
        String moscow = "Moscow";

        Map<Integer, Integer> flightCountByWeekday =
                flightsDao.getFlightCountByWeekdayToCity(moscow);

        System.out.println("in test flight count by weekday to Moscow:");
        System.out.println(flightCountByWeekday);
    }

    @Test
    void deleteFlightsByAircraftModel() throws SQLException {
        String model = "Airbus A321-200";

        int flightCountBefore = flightsDao.getRowCount();
        int ticketFlightCountBefore = ticketFlightsDao.getRowCount();
        int ticketCountBefore = ticketsDao.getRowCount();
        int boardingPassCountBefore = boardingPassesDao.getRowCount();

        ticketsDao.deleteTicketsByAircraftModel(model);
        boardingPassesDao.deletePassesByAircraftModel(model);
        ticketFlightsDao.deleteTicketFlightsByAircraftModel(model);
        flightsDao.deleteFlightsByAircraftModel(model);

        int flightCountAfter = flightsDao.getRowCount();
        int ticketFlightCountAfter = ticketFlightsDao.getRowCount();
        int ticketCountAfter = ticketsDao.getRowCount();
        int boardingPassCountAfter = boardingPassesDao.getRowCount();

        assert(flightCountBefore > flightCountAfter);
        assert(ticketCountBefore > ticketCountAfter);
        assert(ticketFlightCountBefore > ticketFlightCountAfter);
        assert(boardingPassCountBefore > boardingPassCountAfter);
    }

    @Test
    void getLossesAfterCancellingFlightsBetween() throws SQLException, IOException {
        String afterDate = "2017-08-01 00:00:01";
        String beforeDate = "2017-08-15 23:59:59";

        Map<String, Double> lossesByDate = flightsDao.getLossesAfterCancellingFlightsBetween(afterDate,
                beforeDate);

        new LossesAfterCancellingFlights()
                .writeInExcelFile(
                        "./src/main/reports/excel/losses-after-cancelling-flights.xlsx",
                        lossesByDate);

        new LossesByDayChart()
                .drawChart(
                        "./src/main/reports/charts/losses-after-cancelling-flights.png",
                        lossesByDate);

        System.out.println("in test losses counting:");
        System.out.println(lossesByDate);
    }
}
