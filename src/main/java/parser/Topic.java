package parser;

import java.util.Date;

/**
 * The topic class is used as its own data type for the parsing html.
 */
public class Topic {
    private final String title;
    private final String url;
    private final Date date;

    public Topic(String title, String url, Date date) {
        this.title = title;
        this.url = url;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Date getDate() {
        return date;
    }
}
