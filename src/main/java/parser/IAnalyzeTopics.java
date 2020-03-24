package parser;

import java.util.List;

public interface IAnalyzeTopics {

    List<Topic> analyzeTopics(List<Topic> topics, boolean lastYear);

    boolean isTarget(Topic topic);

    boolean isActual(Topic topic, boolean lastYear);
}
