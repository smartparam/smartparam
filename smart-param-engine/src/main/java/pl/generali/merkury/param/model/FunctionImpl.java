package pl.generali.merkury.param.model;

import javax.persistence.*;

/**
 * @author Przemek Hertel
 */
@Entity
@Inheritance
@DiscriminatorColumn(name = "impl")
@Table(name = "par_function_impl")
public abstract class FunctionImpl implements ParamModelObject {

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

    @Transient
    public abstract String getImplCode();
}
