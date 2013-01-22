package org.smartparam.engine.mockBuilders;

import org.smartparam.engine.model.Level;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * {@link Level} mock object builder.
 *
 * @author Adam Dubiel
 * @since 0.1.0
 */
public class LevelMockBuilder {

    private Level level;

    private LevelMockBuilder() {
        this.level = mock(Level.class);
        when(level.isArray()).thenReturn(false);
    }

    public static LevelMockBuilder level() {
        return new LevelMockBuilder();
    }

    public static Level level(String type) {
        return level().withType(type).get();
    }

    public static Level level(String type, boolean array) {
        return level().withType(type).withArray(array).get();
    }

    public Level get() {
        return level;
    }

    public LevelMockBuilder withType(String type) {
        when(level.getType()).thenReturn(type);
        return this;
    }

    public LevelMockBuilder withArray(boolean array) {
        when(level.isArray()).thenReturn(array);
        return this;
    }

    public LevelMockBuilder withLabel(String label) {
        when(level.getLabel()).thenReturn(label);
        return this;
    }

    public LevelMockBuilder withMatcherCode(String matcher) {
        when(level.getMatcherCode()).thenReturn(matcher);
        return this;
    }
}
