package org.smartparam.repository.fs.resolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.smartparam.engine.model.Parameter;
import org.smartparam.repository.fs.ResourceResolver;
import org.smartparam.repository.fs.exception.SmartParamResourceResolverException;
import org.smartparam.serializer.SmartParamDeserializer;
import org.smartparam.serializer.exception.SmartParamSerializationException;

/**
 *
 * @author Adam Dubiel <dubiel.adam@gmail.com>
 */
public class ClasspathResourceResolver implements ResourceResolver {

    private String basePath;

    private String filePattern;

    private SmartParamDeserializer deserializer;

    public ClasspathResourceResolver(String basePath, String filePattern, SmartParamDeserializer deserializer) {
        this.basePath = basePath;
        this.filePattern = filePattern;
        this.deserializer = deserializer;
    }

    @Override
    public Map<String, String> findParameterResources() {
        Reflections reflections = new Reflections(new ResourcesScanner());
        Set<String> resources = reflections.getResources(Pattern.compile(filePattern));

        Map<String, String> parameters = new HashMap<String, String>();
        String parameterName, resourceName;
        for (String resource : resources) {
            resourceName = basePath + resource;
            parameterName = readParameterNameFromResource(resourceName);
            parameters.put(parameterName, resourceName);
        }

        return parameters;
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
            if(stream == null) {
                throw new IOException("no resource " + resourceName + " found");
            }

            reader = new InputStreamReader(stream);
            return deserializer.deserialize(reader);
        } finally {
            if (reader != null) {
                reader.close();
            }
            else if(stream != null) {
                stream.close();
            }
        }
    }
}
