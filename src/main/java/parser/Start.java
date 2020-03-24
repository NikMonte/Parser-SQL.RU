package parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * This class is start point of the program.
 * The Start class performs the task of loading Properties files.
 * Also, it is in this class that the Scheduler
 * is initialized and launched. The class provides the correct exit
 * from the program.
 */
public class Start {
    private static final HashMap<String, Properties> CONFIGS = new HashMap<>();
    private final Logger log = LogManager.getLogger(Start.class);

    public Scheduler initShd(Controller controller) {
        Scheduler scheduler = null;
        try {
            SchedulerFactory sf = new StdSchedulerFactory();
            scheduler = sf.getScheduler();
            JobDetail job = newJob(controller.getClass())
                    .withIdentity("Analyze", "group1")
                    .build();
            String cron = Start.CONFIGS.get("app").getProperty("cron.time");
            CronTrigger trigger = newTrigger()
                    .withIdentity("trigger1", "group1")
                    .withSchedule(cronSchedule(cron))
                    .forJob("Analyze", "group1")
                    .build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
            log.trace(String.format("%s = %s", "Quartz Scheduler is start. Cron mask", cron));
        } catch (SchedulerException e) {
            this.log.error("Problem with Scheduler.");
            this.log.error(e.getMessage());
        }
        return scheduler;
    }

    /**
     * Start initialization Scheduler, wait command for exit and shutdown
     * scheduler.
     */
    public void start(Controller controller) {
        log.trace("Initialization Quartz Scheduler.");
        controller.checkUpdate();
        Scheduler scheduler = this.initShd(controller);
        Scanner scanner = new Scanner(System.in);
        String q = "";
        System.out.println("Press 'q' and 'enter' for exit.");
        while (!"q".equals(q)) {
            q = scanner.next();
        }
        if (scheduler != null) {
            try {
                scheduler.shutdown();
            } catch (SchedulerException e) {
                this.log.error("Problem with Scheduler shutdown.");
                this.log.error(e.getMessage());
            }
        }
        log.trace("Exit.");
    }

    public static Properties getConfig(String key) {
        return Start.CONFIGS.get(key);
    }

    public static void main(String[] args) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        if (args.length > 0 && new File(args[0]).exists() && args[0].endsWith(".properties")) {
            try {
                try (InputStream is = loader.getResourceAsStream("sql.properties");
                     FileInputStream fis = new FileInputStream(args[0])) {
                    Properties app = new Properties();
                    Properties sql = new Properties();
                    sql.load(is);
                    app.load(fis);
                    Start.CONFIGS.put("app", app);
                    Start.CONFIGS.put("sql", sql);
                    Start start = new Start();
                    start.start(new SqlRuJavaSqlStore());
                }
            } catch (IOException e) {
                Logger log = LogManager.getLogger(Start.class);
                log.error("Problem with load properties.");
                log.error(e.getMessage());
            }
        } else {
            System.out.println("Please provide file.properties in first argument and try again.");
        }
    }
}
