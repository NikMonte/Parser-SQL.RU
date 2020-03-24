package parser;

import java.util.List;

public interface StoreSet {

    /**
     * Add not duplicate vacancy to store.
     * @param vacancy The uniqueness of the Vacancy object is
     *              determined by the url field. See the offer
     *              class documentation for more details.
     * @return If the operation is successful, returns id.
     *          If the addition did not occur, returns -1.
     */
    int add(Vacancy vacancy);

    /**
     * Get vacancy from store use parameter id.
     * @param id Id get in added.
     * @return If there is no object, it returns object with empty fields.
     */
    Vacancy get(int id);

    /**
     * The method allows you to check if there is an element.
     * @param vacancy It is enough for the method that the object
     *                does not have an empty url field. Other fields
     *                are not included in the comparison.
     * @return If vacancy contains return true.
     */
    boolean contains(Vacancy vacancy);

    /**
     * Get all vacancy from store.
     * @return List all elements in store.
     */
    List<Vacancy> getAll();

    /**
     * Get all vacancy urls from store.
     * @return List urls all elements in store.
     */
    List<String> getUrls();

    /**
     * isEmpty.
     * @return true if don't exist.
     */
    boolean isEmpty();
}
