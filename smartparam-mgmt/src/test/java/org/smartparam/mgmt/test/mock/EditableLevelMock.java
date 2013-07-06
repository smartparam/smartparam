package org.smartparam.mgmt.test.mock;

import org.smartparam.engine.test.mock.LevelMock;
import org.smartparam.mgmt.model.EditableLevel;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class EditableLevelMock extends LevelMock implements EditableLevel {

    private String label;

    public EditableLevelMock() {
    }

    public EditableLevelMock(String levelCreator, String type, boolean array, String matcherCode) {
        super(levelCreator, type, array, matcherCode);
    }

    public String getName() {
        return label;
    }

    public void setName(String label) {
        this.label = label;
    }
}
