package omae.wa.mou.shindeiru.database;

import java.sql.*;

public class SQLite implements DataBase {
    private Connection connection;

    private final String folder;
    private final String dbName;

    public SQLite(String folder, String dbName) {
        this.folder = folder;
        this.dbName = dbName;
    }

    @Override
    public void connect() {
        String jdbc = "jdbc:sqlite:" + folder + "/" + dbName;

        try {
            Class.forName("org.sqlite.JDBC");

            this.connection = DriverManager.getConnection(jdbc);
            connection.setAutoCommit(true);

            createDefaults();

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ResultSet select(String query) {
        try {
            Statement statement = this.connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void insert(String query) {
        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(String query) {
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createDefaults() {
        insert("CREATE TABLE IF NOT EXISTS autocraft (" +
                "location text," +
                "world text" +
                ");");
    }
}
