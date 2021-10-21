package jdbctask.service.db;

import lombok.AllArgsConstructor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;


@AllArgsConstructor
public class SimpleJdbcTemplate {
    @FunctionalInterface
    public interface SQLFunction<T, R> {
        R apply(T object) throws SQLException;
    }

    @FunctionalInterface
    public interface SQLConsumer<T> {
        void accept(T object) throws SQLException;
    }

    private final DataSource connectionPool;

    public void connection(SQLConsumer<? super Connection> consumer) throws SQLException {
        Objects.requireNonNull(consumer);
        try (Connection conn = connectionPool.getConnection()) {
            consumer.accept(conn);
        }
    }

    public <R> R connection(SQLFunction<? super Connection, ? extends R> function)
            throws SQLException {
        Objects.requireNonNull(function);
        try (Connection conn = connectionPool.getConnection()) {
            return function.apply(conn);
        }
    }

    public <R> R statement(SQLFunction<? super Statement, ? extends R> function)
            throws SQLException {
        Objects.requireNonNull(function);
        return connection(conn -> {
            try (Statement stmt = conn.createStatement()) {
                return function.apply(stmt);
            }
        });
    }

    public void statement(SQLConsumer<? super Statement> consumer) throws SQLException {
        Objects.requireNonNull(consumer);
        connection(conn -> {
            try (Statement stmt = conn.createStatement()) {
                consumer.accept(stmt);
            }
        });
    }

    public <R> R preparedStatement(String sql,
                                   SQLFunction<? super PreparedStatement, ? extends R> function)
            throws SQLException {
        Objects.requireNonNull(function);
        return connection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                return function.apply(stmt);
            }
        });
    }

    public void preparedStatement(String sql,
                                  SQLConsumer<? super PreparedStatement> consumer)
            throws SQLException {
        Objects.requireNonNull(consumer);
        connection(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                consumer.accept(stmt);
            }
        });
    }

    /**
     * Transaction consists of DB modifiers
     * in the end select query is performed.
     * @param consumerQueries is List of modifies in sql
     * @param functionQuery is select query in sql
     * @param consumers - the size must be the same with consumerQueries
     * @param function
     */
    public <R> R preparedTransaction(List<String> consumerQueries, String functionQuery,
                   List<SQLConsumer<? super PreparedStatement>> consumers,
                   SQLFunction<? super PreparedStatement, ? extends R> function)
            throws SQLException {
        for (SQLConsumer consumer : consumers) {
            Objects.requireNonNull(consumer);
        }
        Objects.requireNonNull(function);

        return connection(conn -> {
            conn.setAutoCommit(false);

            for (int i = 0; i < consumerQueries.size(); ++i) {
                try (PreparedStatement stmt = conn.prepareStatement(consumerQueries.get(i))) {
                    consumers.get(i).accept(stmt);
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(functionQuery)) {
                R res = function.apply(stmt);

                conn.commit();
                conn.setAutoCommit(true);

                return res;
            }
        });
    }
}
