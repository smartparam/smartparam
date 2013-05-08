package org.smartparam.engine.bean;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class RepositoryObjectKey implements Comparable<RepositoryObjectKey> {

    private String key;

    private int order;

    public RepositoryObjectKey(String key) {
        this.key = key;
    }

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
        return "[key: " + key + " order: " + order  + "]";
    }


}
