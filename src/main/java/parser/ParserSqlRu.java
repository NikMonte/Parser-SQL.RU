package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The Parser class performs two tasks.
 * The first task is to parse the forum page into topics.
 * The second task is to parse the topic into one single post by the author.
 * This message is expected to contain information about the vacancy.
 */
public class ParserSqlRu implements IParser {
    private static final String URL_SQL_RU = "https://www.sql.ru/forum/job-offers/";
    private final static String[] MONTHS =
            {"янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"};

    @Override
    public List<Topic> parseOnTopics(Document forum) {
        List<Topic> result = new ArrayList<>();
        Elements table = forum.getElementsByClass("forumTable");
        for (Element tr : table.select("tr")) {
            String title = "";
            String url = "";
            String date = "";
            int count = 1;
            for (Element td : tr.select("td")) {
                if (count == 2) {
                    title = td.select("a[href]").first().text();
                    url = td.select("a[href]").first().attr("href");
                } else if (count == 6) {
                    date = td.text().split(",")[0];
                }
                count++;
            }
            if (!"".equals(title) && !"".equals(url) && !"".equals(date)) {
                result.add(new Topic(title, url, this.toDate(date)));
            }
        }
        return result;
    }

    @Override
    public String parseOnMassage(Document topic) {
        StringBuilder result = new StringBuilder();
        Elements msgBody = topic.getElementsByClass("msgBody");
        Element msg = msgBody.select("td").next().first();
        for (TextNode subString : msg.textNodes()) {
            if (!subString.text().equals(" ")) {
                result.append(subString).append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    @Override
    public String getUrlPage(int index) {
        return index == 1 ? ParserSqlRu.URL_SQL_RU : ParserSqlRu.URL_SQL_RU + index;
    }

    private Date toDate(String date) {
        Date result = null;
        if (date.contains("сегодня")) {
            result = new Date();
        } else if (date.contains("вчера")) {
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            result = cal.getTime();
        } else {
            String[] temp = date.split(" ");
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            if (temp.length > 2) {
                int month = 0;
                for (int i = 0; i < ParserSqlRu.MONTHS.length; i++) {
                    if (temp[1].equals(ParserSqlRu.MONTHS[i])) {
                        month = i;
                    }
                }
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp[0]));
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR, Integer.parseInt(temp[2]) + 2000);
            }
            result = calendar.getTime();
        }
        return result;
    }
}
