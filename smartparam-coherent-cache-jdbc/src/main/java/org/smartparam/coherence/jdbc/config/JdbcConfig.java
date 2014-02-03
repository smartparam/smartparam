package org.smartparam.coherence.jdbc.config;

import org.polyjdbc.core.dialect.Dialect;

public class JdbcConfig {

    private final Dialect dialect;

    private String entityPrefix = "sp_";

    private String sequencePrefix = "seq_";

    private String indexPrefix = "idx_";

    private String primaryKeyPrefix = "pk_";

    private String foreignKeyPrefix = "fk_";

    private String sufix = "cached_param_version";

    public JdbcConfig(Dialect dialect) {
        this.dialect = dialect;
    }

    public Dialect dialect() {
        return dialect;
    }

    public String entityName() {
        return entityPrefix + sufix;
    }

    public String sequenceName() {
        return sequencePrefix + sufix;
    }

    void sufix(String sufix) {
        this.sufix = sufix;
    }

    void entityPrefix(String entityPrefix) {
        this.entityPrefix = entityPrefix;
    }

    void sequencePrefix(String sequencePrefix) {
        this.sequencePrefix = sequencePrefix;
    }

    public String indexPrefix() {
        return indexPrefix;
    }

    void indexPrefix(String indexPrefix) {
        this.indexPrefix = indexPrefix;
    }

    public String primaryKeyPrefix() {
        return primaryKeyPrefix;
    }

    void primaryKeyPrefix(String primaryKeyPrefix) {
        this.primaryKeyPrefix = primaryKeyPrefix;
    }

    public String foreignKeyPrefix() {
        return foreignKeyPrefix;
    }

    void foreignKeyPrefix(String foreignKeyPrefix) {
        this.foreignKeyPrefix = foreignKeyPrefix;
    }

}
