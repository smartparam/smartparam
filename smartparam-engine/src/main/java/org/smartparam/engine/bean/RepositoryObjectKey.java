package org.smartparam.engine.bean;

/**
 * Complex key that can be used in maps that need only unique entries and
 * custom ordering at the same time.
 * Object consists of:
 * <ul>
 * <li>key - string name, that can be used to preserve uniqueness of entries</li>
 * <li>order - integer value, that can be used for ordering</li>
 * </ul>
 *
 * Only key is used to compute hash key and determine equality of two objects.
 * Only order is used in comparison method from {@link Comparable} interface.
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 * @since 0.1.0
 */
public class RepositoryObjectKey implements Comparable<RepositoryObjectKey> {

    /**
     * Key of entry, should be unique across the container.
     */
    private String key;

    /**
     * Order number, defaults to 0.
     */
    private int order;

    /**
     * Create new repository key with only string key.
     *
     * @param key unique key
     */
    public RepositoryObjectKey(String key) {
        this.key = key;
    }

    /**
     * Create new repository key with string key and order.
     *
     * @param key   unique key
     * @param order order of item if sorted
     */
    public RepositoryObjectKey(String key, int order) {
        this.key = key;
        this.order = order;
    }

    public String getKey() {
        return key;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public int compareTo(RepositoryObjectKey other) {
        if (other.order == order) {
            return key.compareTo(other.key);
        }
        return order > other.order ? 1 : -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RepositoryObjectKey) {
            return key.equals(((RepositoryObjectKey) obj).key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.key != null ? this.key.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "[key: " + key + " order: " + order + "]";
    }
}
