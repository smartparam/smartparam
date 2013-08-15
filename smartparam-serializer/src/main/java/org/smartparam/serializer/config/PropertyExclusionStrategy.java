package org.smartparam.serializer.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Adam Dubiel
 */
public class PropertyExclusionStrategy implements ExclusionStrategy {

    private Set<String> propertiesToExclude = new HashSet<String>();

    public PropertyExclusionStrategy(String... propertiesToExclude) {
        this.propertiesToExclude.addAll(Arrays.asList(propertiesToExclude));
    }

    @Override
    public boolean shouldSkipField(FieldAttributes arg0) {
        return propertiesToExclude.contains(arg0.getName());
    }

    @Override
    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }
}
