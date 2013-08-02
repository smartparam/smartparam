package org.smartparam.repository.fs.resolver;

import com.google.common.base.Predicates;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smartparam.engine.model.Parameter;
import org.smartparam.repository.fs.ResourceResolver;
import org.smartparam.repository.fs.exception.SmartParamResourceResolverException;
import org.smartparam.serializer.ParamDeserializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ClasspathResourceResolver implements ResourceResolver {

    private static final Logger logger = LoggerFactory.getLogger(ClasspathResourceResolver.class);

    private static final String CLASSPATH_SEPARATOR = "/";

    private static final String PACKAGE_SEPARATOR = ".";

    private String basePath;

    private String filePattern;

    private ParamDeserializer deserializer;

    public ClasspathResourceResolver(String basePath, String filePattern, ParamDeserializer deserializer) {
        this.basePath = basePath;
        this.filePattern = filePattern;
        this.deserializer = deserializer;
    }

    @Override
    public Map<String, String> findParameterResources() {
        logger.info("scanning resources, filtering pattern: {}", filePattern);

        String packagePath = createPackagePath(basePath);
        String regexPackagePath = "^" + packagePath.replaceAll("\\" + PACKAGE_SEPARATOR, "[" + PACKAGE_SEPARATOR + "]");

        Configuration config = new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packagePath))
                .filterInputsBy(Predicates.containsPattern(regexPackagePath))
                .addScanners(new ResourcesScanner());
        Reflections reflections = new Reflections(config);

        Set<String> resources = reflections.getResources(Pattern.compile(filePattern));

        Map<String, String> parameters = new HashMap<String, String>();
        String parameterName, resourceName;
        for (String resource : resources) {
            resourceName = CLASSPATH_SEPARATOR + resource;
            parameterName = readParameterNameFromResource(resourceName);

            parameters.put(parameterName, resourceName);
            logger.debug("found parameter {} in resource {}", parameterName, resourceName);
        }

        return parameters;
    }

    private String createPackagePath(String basePath) {
        String packagePath = "";
        if (basePath.length() > 1) {
            packagePath = basePath.replaceAll(CLASSPATH_SEPARATOR, PACKAGE_SEPARATOR);
            if(packagePath.startsWith(PACKAGE_SEPARATOR)) {
                packagePath = packagePath.substring(1);
            }
            if(!packagePath.endsWith(PACKAGE_SEPARATOR)) {
                packagePath += PACKAGE_SEPARATOR;
            }
        }
        return packagePath;
    }

    private String readParameterNameFromResource(String resourceName) {
        try {
            return readParameterConfigFromResource(resourceName).getName();
        } catch (IOException ioException) {
            throw new SmartParamResourceResolverException("unable to load parameter from " + resourceName, ioException);
        } catch (SmartParamSerializationException serializationException) {
            throw new SmartParamResourceResolverException("unable to load parameter from " + resourceName, serializationException);
        }
    }

    private Parameter readParameterConfigFromResource(String resourceName) throws IOException, SmartParamSerializationException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(resourceName)));
            return deserializer.deserializeConfig(reader);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    @Override
    public Parameter loadParameterFromResource(String parameterResourceName) {
        try {
            return readParameterFromResource(parameterResourceName);
        } catch (IOException ioException) {
            throw new SmartParamResourceResolverException("unable to load parameter from " + parameterResourceName, ioException);
        } catch (SmartParamSerializationException serializationException) {
            throw new SmartParamResourceResolverException("unable to load parameter from " + parameterResourceName, serializationException);
        }
    }

    private Parameter readParameterFromResource(String resourceName) throws IOException, SmartParamSerializationException {
        InputStream stream = null;
        Reader reader = null;
        try {
            stream = this.getClass().getResourceAsStream(resourceName);
            if (stream == null) {
                throw new IOException("no resource " + resourceName + " found");
            }

            reader = new InputStreamReader(stream);
            return deserializer.deserialize(reader);
        } finally {
            if (reader != null) {
                reader.close();
            } else if (stream != null) {
                stream.close();
            }
        }
    }
}
