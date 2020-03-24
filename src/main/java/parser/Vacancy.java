package parser;

import java.util.Objects;

/**
 * The vacancy class is used as its own data type for the repository and canonical
 * representation of the vacancy located on the forum.
 */
public class Vacancy {
    private final String name;
    private final String text;
    private final String url;

    public Vacancy(String name, String text, String url) {
        this.name = name;
        this.text = text;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        String ls = System.lineSeparator();
        StringBuilder sb = new StringBuilder();
        sb.append(ls).append("Vacancy name = ").append(String.format("\"%s\"", name));
        sb.append(ls).append("Vacancy url = ").append(String.format("\"%s\"", url));
        sb.append(ls).append("Vacancy text = ").append(String.format("\"%s\"", text));
        return sb.append(ls).toString();
    }

    /**
     *  The Vacancy object in the equals () and hasCode () methods
     * uses the standard String.equals for the url field.
     * @param o Vacancy.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vacancy vacancy = (Vacancy) o;
        return Objects.equals(url, vacancy.url);
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
