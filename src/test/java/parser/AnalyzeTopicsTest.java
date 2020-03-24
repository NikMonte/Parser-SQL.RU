package parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.is;

public class AnalyzeTopicsTest {
    private final AnalyzeTopics analyzeTopics = new AnalyzeTopics();
    private final Topic topic1 = new Topic("...", "", new Date());
    private final Topic topic2 = new Topic("... Java Developer ...", "", new Date());
    private Topic topic3;
    private Topic topic4;
    private List<Topic> arr;
    private List<Topic> exp;

    @Before
    public void setUp() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -20);
        topic3 = new Topic("... Java Script ...", "", cal.getTime());
        cal.add(Calendar.YEAR, 60);
        topic4 = new Topic("... JavaScript ...", "", cal.getTime());
        Topic topic5 = new Topic("... Java ...", "", cal.getTime());
        arr = Arrays.asList(topic1, topic2, topic3, topic4, topic5);
        exp = Arrays.asList(topic2, topic5);
    }

    @Test
    public void whenUseIsTargetThenReturnTrueOrFalse() {
        Assert.assertThat(analyzeTopics.isTarget(topic1), is(false));
        Assert.assertThat(analyzeTopics.isTarget(topic2), is(true));
        Assert.assertThat(analyzeTopics.isTarget(topic3), is(false));
        Assert.assertThat(analyzeTopics.isTarget(topic4), is(false));
    }

    @Test
    public void whenUseIsActualThenReturnTrueOrFalse() {
        Assert.assertThat(analyzeTopics.isActual(topic1, false), is(true));
        Assert.assertThat(analyzeTopics.isActual(topic2, false), is(true));
        Assert.assertThat(analyzeTopics.isActual(topic3, false), is(false));
        Assert.assertThat(analyzeTopics.isActual(topic1, true), is(true));
        Assert.assertThat(analyzeTopics.isActual(topic3, true), is(false));
        Assert.assertThat(analyzeTopics.isActual(topic4, true), is(true));
    }

    @Test
    public void whenUseAnalyzeTopicsWithLastYearAndDontThenReturnList() {
        Assert.assertThat(analyzeTopics.analyzeTopics(arr, false), is(exp));
        Assert.assertThat(analyzeTopics.analyzeTopics(arr, true), is(exp));
    }
}
