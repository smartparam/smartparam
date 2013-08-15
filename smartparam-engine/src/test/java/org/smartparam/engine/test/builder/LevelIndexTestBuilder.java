package org.smartparam.engine.test.builder;

import java.util.ArrayList;
import java.util.List;
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.index.Matcher;
import org.smartparam.engine.core.type.Type;

/**
 *
 * @author Adam Dubiel
 */
public class LevelIndexTestBuilder {

    private int levels;

    private List<Type<?>> indexTypes = new ArrayList<Type<?>>();

    private List<Matcher> levelMatchers = new ArrayList<Matcher>();

    private LevelIndexTestBuilder() {
    }

    public static LevelIndexTestBuilder levelIndex() {
        return new LevelIndexTestBuilder();
    }

    public <T> LevelIndex<T> build() {
        Type<?>[] types = indexTypes.toArray(new Type<?>[indexTypes.size()]);
        Matcher[] matchers = levelMatchers.toArray(new Matcher[levelMatchers.size()]);

        return new LevelIndex<T>(levels, types, matchers);
    }

    public LevelIndexTestBuilder withLevelCount(int levelCount) {
        this.levels = levelCount;
        return this;
    }

    public LevelIndexTestBuilder withMatcher(Matcher matcher) {
        levelMatchers.add(matcher);
        return this;
    }

    public LevelIndexTestBuilder withType(Type<?> type) {
        indexTypes.add(type);
        return this;
    }
}
