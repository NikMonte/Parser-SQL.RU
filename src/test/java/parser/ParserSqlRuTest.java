package parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.Matchers.is;

public class ParserSqlRuTest {
    private File topics;
    private File topicFirst;
    private File topicTwo;
    private String expectFirst;
    private String expectTwo;
    private ParserSqlRu parserSqlRu = new ParserSqlRu();

    @Before
    public void setUp() throws IOException, URISyntaxException {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        topicFirst = new File(loader.getResource("topicFirst.html").getFile());
        topicTwo = new File(loader.getResource("topicTwo.html").getFile());
        topics = new File(loader.getResource("topics.html").getFile());
        expectFirst = Files.readString(Paths.get(loader.getResource("expectFirst.txt").toURI()));
        expectTwo = new StringBuilder()
                .append(" Хотел бы сообщить пользователю ")
                .append(System.lineSeparator())
                .append(" о том, что несмотря на создание подфорума, \"девять\" по-прежнему больше \"пяти\". ")
                .append(System.lineSeparator())
                .toString();
    }

    @Test
    public void parseOnTopics() throws IOException {
        Document doc = Jsoup.parse(topics, "windows-1251");
        List<Topic> list = parserSqlRu.parseOnTopics(doc);
        Assert.assertThat(list.size(), is(53));
        Assert.assertThat(list.get(0).getTitle(), is("Сообщения от модераторов (здесь Вы можете узнать причины удаления топиков)"));
        Assert.assertThat(list.get(0).getUrl(), is("https://www.sql.ru/forum/485068/soobshheniya-ot-moderatorov-zdes-vy-mozhete-uznat-prichiny-udaleniya-topikov"));
        Assert.assertThat(list.get(1).getTitle(), is("Шпаргалки"));
        Assert.assertThat(list.get(1).getUrl(), is("https://www.sql.ru/forum/1196621/shpargalki"));
        Assert.assertThat(list.get(26).getTitle(), is("Java разработчик, ИТБ банк (Москва)"));
        Assert.assertThat(list.get(26).getUrl(), is("https://www.sql.ru/forum/1316572/java-razrabotchik-itb-bank-moskva"));
        Assert.assertThat(list.get(52).getTitle(), is("Разработчик MS SQL (Junior, Москва)"));
        Assert.assertThat(list.get(52).getUrl(), is("https://www.sql.ru/forum/1316360/razrabotchik-ms-sql-junior-moskva"));
    }

    @Test
    public void parseTopic() throws IOException {
        Document doc = Jsoup.parse(topicFirst, "windows-1251");
        Document doc2 = Jsoup.parse(topicTwo, "windows-1251");
        Assert.assertThat(parserSqlRu.parseOnMassage(doc), is(expectFirst)); // Parse first msg in job topic.
        Assert.assertThat(parserSqlRu.parseOnMassage(doc2), is(expectTwo)); // Parse first msg in moderator's topic.
    }
}
