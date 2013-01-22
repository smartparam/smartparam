package org.smartparam.provider.hibernate.model;

import javax.persistence.*;
import org.smartparam.engine.model.FunctionImpl;

/**
 * @author Przemek Hertel
 */
@Entity
@Inheritance
@DiscriminatorColumn(name = "impl")
@Table(name = "par_function_impl")
public abstract class HibernateFunctionImpl implements FunctionImpl, ParamModelObject {

    /**
     * SUID.
     */
    private static final long serialVersionUID = 1L;

    private int id;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_function_impl")
    @SequenceGenerator(name = "seq_function_impl", sequenceName = "seq_function_impl")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
