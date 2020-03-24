package parser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Sql store.
 */
public class SqlStore implements StoreSet, AutoCloseable {
    private final Connection conn;
    private final Properties sql;

    public SqlStore(Connection conn, Properties sql) {
        this.conn = conn;
        this.sql = sql;
    }

    @Override
    public int add(Vacancy vacancy) {
        int result = -1;
        if (!this.contains(vacancy)) {
            try (PreparedStatement st = conn.prepareStatement(this.sql.getProperty("sql.add_vacancy"),
                    Statement.RETURN_GENERATED_KEYS)) {
                st.setString(1, vacancy.getName());
                st.setString(2, vacancy.getText());
                st.setString(3, vacancy.getUrl());
                st.executeUpdate();
                try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        result = generatedKeys.getInt(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public Vacancy get(int id) {
        Vacancy result = new Vacancy("", "", "");
        try (PreparedStatement st = conn.prepareStatement(this.sql.getProperty("sql.get_vacancy_by_id"))) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    result = new Vacancy(rs.getString("name"), rs.getString("text"), rs.getString("url"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean contains(Vacancy vacancy) {
        boolean result = false;
        try (PreparedStatement st = conn.prepareStatement(this.sql.getProperty("sql.get_vacancy_by_url"))) {
            st.setString(1, vacancy.getUrl());
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    result = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Vacancy> getAll() {
        ArrayList<Vacancy> result = new ArrayList<>();
        try (Statement st = this.conn.createStatement()) {
            try (ResultSet rs = st.executeQuery(this.sql.getProperty("sql.get_all_vacancy"))) {
                while (rs.next()) {
                    result.add(new Vacancy(rs.getString("name"), rs.getString("text"), rs.getString("url")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<String> getUrls() {
        ArrayList<String> result = new ArrayList<>();
        try (Statement st = this.conn.createStatement()) {
            try (ResultSet rs = st.executeQuery(this.sql.getProperty("sql.get_all_vacancy_urls"))) {
                while (rs.next()) {
                    result.add(rs.getString("url"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean isEmpty() {
        boolean result = false;
        try (Statement st = this.conn.createStatement()) {
            try (ResultSet rs = st.executeQuery(this.sql.getProperty("sql.is_empty"))) {
                if (rs.next()) {
                    result = rs.getInt("count") == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }
}
