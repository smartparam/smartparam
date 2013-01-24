package org.smartparam.provider.jpa.model;

import javax.persistence.*;
import org.smartparam.engine.model.FunctionImpl;

/**
 * @author Przemek Hertel
 */
@Entity
@Inheritance
@DiscriminatorColumn(name = "impl")
@Table(name = "smartpar_function_impl")
public abstract class JpaFunctionImpl implements FunctionImpl, JpaModelObject {

    /**
     * SUID.
     */
    private static final long serialVersionUID = 1L;

    private int id;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sp_function_impl")
    @SequenceGenerator(name = "seq_sp_function_impl", sequenceName = "seq_sp_function_impl")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
