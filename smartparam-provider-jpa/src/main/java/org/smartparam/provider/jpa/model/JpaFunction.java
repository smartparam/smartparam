package org.smartparam.provider.jpa.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.smartparam.engine.model.Function;

/**
 * JPA data source based function implementation.
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
@Entity
@Table(name = "smartparam_function")
@NamedQuery(name=JpaFunction.LOAD_FUNCTION_QUERY, query="from JpaFunction where name = :name")
public class JpaFunction implements Function, JpaModelObject {

    /**
     * Approximate length of toString() output, string buffer initial capacity.
     */
    private static final int TO_STRING_APPROX_LENGTH = 40;

    /**
     * Identifier of named query fetching function using its name.
     */
    public static final String LOAD_FUNCTION_QUERY = "smartparamLoadFunction";

    /**
     * SUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Function database id.
     */
    private int id;

    /**
     * Unique function name.
     */
    private String name;

    /**
     * Optional function type matching {@link org.smartparam.engine.core.config.TypeProvider} type.
     */
    private String type;

    /**
     * Is function returning parameter versioning date.
     */
    private boolean versionSelector;

    /**
     * Is function a level creator (dynamic level value evaluator).
     */
    private boolean levelCreator;

    /**
     * Is this a plugin function.
     */
    private boolean plugin;

    /**
     * Function implementation.
     */
    private JpaFunctionImpl implementation;

    /**
     * Id getter.
     *
     * @return id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sp_function")
    @SequenceGenerator(name = "seq_sp_function", sequenceName = "seq_sp_function")
    public int getId() {
        return id;
    }

    /**
     * Id setter.
     *
     * @param id id
     */
    public void setId(int id) {
        this.id = id;
    }

    @Override
    @Column
    public boolean isLevelCreator() {
        return levelCreator;
    }

    /**
     * Level creator setter.
     *
     * @param levelCreator is level creator
     */
    public void setLevelCreator(boolean levelCreator) {
        this.levelCreator = levelCreator;
    }

    @Override
    @Column(unique = true)
    public String getName() {
        return name;
    }

    /**
     * Name setter.
     *
     * @param name unique name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    @Column(length = SHORT_COLUMN_LENGTH)
    public String getType() {
        return type;
    }

    /**
     * Function type setter.
     *
     * @param type type
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    @Column
    public boolean isVersionSelector() {
        return versionSelector;
    }

    /**
     * Is function a version selector.
     *
     * @param versionSelector is version selector
     */
    public void setVersionSelector(boolean versionSelector) {
        this.versionSelector = versionSelector;
    }

    @Override
    @Column
    public boolean isPlugin() {
        return plugin;
    }

    /**
     * Is plugin function.
     *
     * @param plugin is plugin function
     */
    public void setPlugin(boolean plugin) {
        this.plugin = plugin;
    }

    @Override
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    public JpaFunctionImpl getImplementation() {
        return implementation;
    }

    /**
     * Implementation setter.
     *
     * @param implementation implementation
     */
    public void setImplementation(JpaFunctionImpl implementation) {
        this.implementation = implementation;
    }

    /**
     * Used by {@link #toString()}, appends flag values.
     *
     * @param sb string builder to append to
     */
    private void appendFlags(StringBuilder sb) {
        if (isVersionSelector() || isLevelCreator() || isPlugin()) {
            sb.append(", flags=");
            if (isVersionSelector()) {
                sb.append('V');
            }
            if (isLevelCreator()) {
                sb.append('L');
            }
            if (isPlugin()) {
                sb.append('P');
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(TO_STRING_APPROX_LENGTH);

        sb.append("Function#").append(id);
        sb.append("[name=").append(name);
        sb.append(", type=").append(type);
        appendFlags(sb);
        sb.append(']');

        return sb.toString();
    }
}
