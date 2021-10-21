import jdbctask.domain.*;
import jdbctask.service.dao.AircraftDao;
import jdbctask.service.dao.AirportDao;
import jdbctask.service.dao.BookingsDao;
import jdbctask.service.dao.BoardingPassesDao;
import jdbctask.service.dao.FlightsDao;
import jdbctask.service.dao.SeatsDao;
import jdbctask.service.dao.TicketFlightsDao;
import jdbctask.service.dao.TicketsDao;

import jdbctask.service.db.SimpleJdbcTemplate;
import jdbctask.service.db.DbCreateInit;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

class DbCreateInitTest {
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(
            JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1", "", ""));

    private AircraftDao aircraftDao = new AircraftDao(source);
    private AirportDao airportDao = new AirportDao(source);
    private BoardingPassesDao boardingPassesDao = new BoardingPassesDao(source);
    private BookingsDao bookingsDao = new BookingsDao(source);
    private FlightsDao flightsDao = new FlightsDao(source);
    private SeatsDao seatsDao = new SeatsDao(source);
    private TicketFlightsDao ticketFlightsDao = new TicketFlightsDao(source);
    private TicketsDao ticketsDao = new TicketsDao(source);

    @BeforeAll
    public static void setupDB() throws IOException, SQLException {
        new DbCreateInit(source).create();
    }

    @AfterAll
    public static void tearDownDB() throws SQLException, IOException {
        source.statement(stmt -> {
            stmt.execute("drop all objects;");
        });
    }

    @Test
    void initDb() throws IOException, SQLException {
        new DbCreateInit(source).init();
    }

    @Test
    void getAircrafts() throws SQLException {
        Set<Aircraft> aircrafts = aircraftDao.getAircrafts();
        assert(aircrafts.size() == 9);
    }

    @Test
    void getAirports() throws SQLException {
        Set<Airport> airports = airportDao.getAirports();
        assert(airports.size() == 104);
    }

    @Test
    void getBoardingPasses() throws SQLException {
        Set<BoardingPass> boardingPasses = boardingPassesDao.getBoardingPasses();
        assert(boardingPasses.size() == 579686);
    }

    @Test
    void getBookings() throws SQLException {
        Set<Booking> bookings = bookingsDao.getBookings();
        assert(bookings.size() == 262788);
    }

    @Test
    void getFlights() throws SQLException {
        Set<Flight> flights = flightsDao.getFlights();
        assert(flights.size() == 33121);
    }

    @Test
    void getSeats() throws SQLException {
        Set<Seat> seats = seatsDao.getSeats();
        assert(seats.size() == 1339);
    }

    @Test
    void getTicketFlights() throws SQLException {
        Set<TicketFlight> ticketFlights = ticketFlightsDao.getTicketFlights();
        assert(ticketFlights.size() > 0);
    }

    @Test
    void getTickets() throws SQLException {
        Set<Ticket> tickets = ticketsDao.getTickets();
        assert(tickets.size() > 0);
    }
}
