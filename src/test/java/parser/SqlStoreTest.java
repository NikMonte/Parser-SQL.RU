package parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.Matchers.is;

public class SqlStoreTest {
    private Properties sql = new Properties();
    private Properties app = new Properties();
    private Connection rollBack;
    private Vacancy vacancy1 = new Vacancy("name", "text", "url");
    private Vacancy vacancy2 = new Vacancy("not name", "not text", "not url");


    @Before
    public void setUp() throws Exception {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        try (InputStream appFis = loader.getResourceAsStream("app.properties");
             InputStream sqlFis = loader.getResourceAsStream("sql.properties")) {
            sql.load(sqlFis);
            app.load(appFis);
            Class.forName(app.getProperty("jdbc.driver"));
            Connection standard = DriverManager.getConnection(
                    app.getProperty("jdbc.url"),
                    app.getProperty("jdbc.username"),
                    app.getProperty("jdbc.password"));
            rollBack = ConnectionRollback.create(standard);
        }
    }

    @Test
    public void whenAddThenReturnIdOrMinusOne() throws Exception {
        try (SqlStore store = new SqlStore(rollBack, sql)) {
            Assert.assertThat(store.isEmpty(), is(true));
            Assert.assertThat(store.add(vacancy1) != -1, is(true));
            Assert.assertThat(store.isEmpty(), is(false));
            Assert.assertThat(store.add(vacancy1) == -1, is(true));
        }
    }

    @Test
    public void whenGetThenReturnVacancyOrEmptyVacancy() throws Exception {
        try (SqlStore store = new SqlStore(rollBack, sql)) {
            int result = store.add(vacancy1);
            Assert.assertThat(store.get(result), is(vacancy1));
            Assert.assertThat(store.get(-1), is(new Vacancy("", "", "")));
        }
    }

    @Test
    public void whenContainsThenReturnTrueOrFalse() throws Exception {
        try (SqlStore store = new SqlStore(rollBack, sql)) {
            store.add(vacancy1);
            Assert.assertThat(store.contains(vacancy1), is(true));
            Assert.assertThat(store.contains(new Vacancy(" ", " ", " ")), is(false));
        }
    }

    @Test
    public void whenGetAllThenReturnNotEmptyList() throws Exception {
        try (SqlStore store = new SqlStore(rollBack, sql)) {
            store.add(vacancy1);
            store.add(vacancy2);
            List<Vacancy> expected = Arrays.asList(vacancy1, vacancy2);
            Assert.assertThat(store.getAll(), is(expected));
        }
    }

    @Test
    public void whenGetUrlsThenReturnNotEmptyList() throws Exception {
        try (SqlStore store = new SqlStore(rollBack, sql)) {
            store.add(vacancy1);
            store.add(vacancy2);
            List<String> expected = Arrays.asList(vacancy1.getUrl(), vacancy2.getUrl());
            Assert.assertThat(store.getUrls(), is(expected));
        }
    }
}
