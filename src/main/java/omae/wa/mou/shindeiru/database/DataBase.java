package omae.wa.mou.shindeiru.database;

import java.sql.ResultSet;

public interface DataBase {

    void connect();

    ResultSet select(String query);
    void insert(String query);
    void update(String query);

    void disconnect();
}
