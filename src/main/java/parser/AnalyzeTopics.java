package parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The Analyze Topics class checks topics for relevance and java to the word in the title
 */
public class AnalyzeTopics implements IAnalyzeTopics {

    @Override
    public List<Topic> analyzeTopics(List<Topic> topics, boolean lastYear) {
        ArrayList<Topic> result = new ArrayList<>();
        for (Topic topic : topics) {
            if (this.isTarget(topic) && this.isActual(topic, lastYear)) {
                result.add(topic);
            }
        }
        return result;
    }

    @Override
    public boolean isTarget(Topic topic) {
        return topic.getTitle().toLowerCase().contains("java")
                && !topic.getTitle().toLowerCase().contains("java script")
                && !topic.getTitle().toLowerCase().contains("javascript");
    }

    @Override
    public boolean isActual(Topic topic, boolean lastYear) {
        Calendar cal = Calendar.getInstance();
        Date last;
        if (lastYear) {
            cal.add(Calendar.YEAR, -1);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            last = cal.getTime();
        } else {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            last = cal.getTime();
        }
        return last.compareTo(topic.getDate()) < 0;
    }
}
