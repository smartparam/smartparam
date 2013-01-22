package org.smartparam.provider.hibernate.model;

import java.io.Serializable;

/**
 * @author Przemek Hertel
 */
public interface ParamModelObject extends Serializable {

    int LONG_COLUMN_LENGTH = 4000;

    int MEDIUM_COLUMN_LENGTH = 100;

    int SHORT_COLUMN_LENGTH = 50;

}
