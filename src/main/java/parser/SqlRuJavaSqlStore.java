package parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

public class SqlRuJavaSqlStore implements Controller  {
    private final Logger log = LogManager.getLogger(Start.class);

    public SqlRuJavaSqlStore() {
        // Instances of Job must have a public no-argument constructor.
    }

    public void checkUpdate() {
        Properties app = Start.getConfig("app");
        Properties sql = Start.getConfig("sql");
        try {
            Class.forName(app.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            log.error("Problem with jdbc driver.");
            log.error(e.getMessage());
        }
        try (Connection connection = DriverManager.getConnection(
                app.getProperty("jdbc.url"),
                app.getProperty("jdbc.username"),
                app.getProperty("jdbc.password"));
             SqlStore store = new SqlStore(connection, sql)) {
            boolean isEmpty = store.isEmpty();
            IParser parser = new ParserSqlRu();
            IAnalyzeTopics analyze = new AnalyzeTopics();
            CollectVacancies collectVac = new CollectVacancies(parser, analyze);
            List<Topic> topics = collectVac.searchTargetTopics(isEmpty);
            List<Vacancy> vacancies = collectVac.topicsToVacancy(topics);
            collectVac.saveVacancies(store, vacancies);
        } catch (Exception e) {
            this.log.error("Problem with Store.");
            this.log.error(e.getMessage());
        }
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Logger log = LogManager.getLogger(SqlRuJavaSqlStore.class);
        log.trace("Check update.");
        this.checkUpdate();
        log.trace("Check update complete.");
    }
}
