package org.smartparam.provider.jpa.model;

import javax.persistence.*;
import org.smartparam.engine.model.function.FunctionImpl;

/**
 * Base class for all function implementations, extending classes
 * should provide a unique function implementation type to act as a
 * discriminator.
 *
 * @author Przemek Hertel
 * @author Adam Dubiel
 * @since 0.1.0
 */
@Entity
@Inheritance
@DiscriminatorColumn(name = "impl")
@Table(name = "smartparam_function_impl")
public abstract class JpaFunctionImpl implements FunctionImpl, JpaModelObject {

    /**
     * SUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Database id.
     */
    private int id;

    /**
     * Id getter.
     *
     * @return id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sp_function_impl")
    @SequenceGenerator(name = "seq_sp_function_impl", sequenceName = "seq_sp_function_impl")
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
}
