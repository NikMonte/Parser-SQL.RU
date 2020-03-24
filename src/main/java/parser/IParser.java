package parser;

import org.jsoup.nodes.Document;

import java.util.List;

public interface IParser {

    List<Topic> parseOnTopics(Document forum);

    String parseOnMassage(Document topic);

    String getUrlPage(int index);
}
