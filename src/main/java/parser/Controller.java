package parser;

import org.quartz.Job;

public interface Controller extends Job {

    /**
     * This method performs all the steps for placing new vacancies in the store.
     */
    void checkUpdate();
}
