package parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The CollectVacancies class converts a topic into a vacancy,
 * saves them in a storehouse. The class via Jsoup connects
 * to the Internet to get the text of the topic pages and
 * the forum pages with work. The class interacts with the
 * Parser, an advanced document, so the Parser does not deal
 * with the issue of receiving an html document. The class
 * also interacts with interface AnalyzeTopics to filter only current
 * topics and target topics.
 */
public class CollectVacancies {
    public final Logger log = LogManager.getLogger(CollectVacancies.class);
    private final IParser parser;
    private final IAnalyzeTopics analyzeTopics;

    public CollectVacancies(IParser parser, IAnalyzeTopics analyzeTopics) {
        this.parser = parser;
        this.analyzeTopics = analyzeTopics;
    }

    public List<Topic> searchTargetTopics(boolean lastYear) {
        List<Topic> jTopics = new ArrayList<>();
        Document forum = this.urlToDoc(parser.getUrlPage(1));
        if (forum != null) {
            log.trace("Parse and analyze topics.");
            List<Topic> topics = this.parser.parseOnTopics(forum);
            jTopics = this.analyzeTopics.analyzeTopics(topics, lastYear);
            Topic lastOnPage = topics.get(topics.size() - 1);
            int countPage = 2;
            while (this.analyzeTopics.isActual(lastOnPage, lastYear)) {
                Document nextPage = this.urlToDoc(parser.getUrlPage(countPage++));
                if (nextPage != null) {
                    topics = this.parser.parseOnTopics(nextPage);
                    jTopics.addAll(this.analyzeTopics.analyzeTopics(topics, lastYear));
                    lastOnPage = topics.get(topics.size() - 1);
                } else {
                    break;
                }
            }
        }
        return jTopics;
    }

    public List<Vacancy> topicsToVacancy(List<Topic> topics) {
        ArrayList<Vacancy> result = new ArrayList<>();
        for (Topic topic : topics) {
            Document topicPage = this.urlToDoc(topic.getUrl());
            if (topicPage != null) {
                String text = this.parser.parseOnMassage(topicPage);
                result.add(new Vacancy(topic.getTitle(), text, topic.getUrl()));
            }
        }
        return result;
    }

    public void saveVacancies(StoreSet store, List<Vacancy> vacancies) {
        for (Vacancy vacancy : vacancies) {
            int i = store.add(vacancy);
            if (i != -1) {
                log.trace("Vacancy saved in store.");
                log.trace(vacancy);
            }
        }
    }

    public Document urlToDoc(String url) {
        Document forum = null;
        try {
            forum = Jsoup.connect(url).get();
        } catch (IOException e) {
            log.error("Problem with connect. Exit programme.");
            log.error(e.getMessage());
        }
        return forum;
    }
}
