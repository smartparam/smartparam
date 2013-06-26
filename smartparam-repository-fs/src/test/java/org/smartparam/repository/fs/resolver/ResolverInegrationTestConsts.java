package org.smartparam.repository.fs.resolver;

import java.io.File;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public abstract class ResolverInegrationTestConsts {

    protected static final String PARAMETER_SUB_DIR_NAME = "paramSubdir1";

    protected static final String PARAMETER_DEEP_SUB_DIR_NAME = "paramSubdir2";

    protected static final String CLASSPATH_SEPARATOR = "/";

    protected String createFilePath(String... components) {
        return StringUtils.join(components, File.separatorChar) + File.separatorChar;
    }

    protected String createPath(String... components) {
        return StringUtils.join(components, CLASSPATH_SEPARATOR) + CLASSPATH_SEPARATOR;
    }
}
