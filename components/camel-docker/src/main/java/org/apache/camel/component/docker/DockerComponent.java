/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.docker;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.component.docker.exception.DockerException;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link DockerEndpoint}.
 */
public class DockerComponent extends DefaultComponent {

    private DockerConfiguration configuration;

    public DockerComponent() {

    }

    public DockerComponent(DockerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {

        // Each endpoint can have its own configuration so make
        // a copy of the configuration
        DockerConfiguration configuration = getConfiguration().copy();

        String normalizedRemaining = remaining.replaceAll("/", "");

        DockerOperation operation = DockerOperation.getDockerOperation(normalizedRemaining);

        if (operation == null) {
            throw new DockerException(remaining + " is not a valid operation");
        }

        configuration.setOperation(operation);

        // Validate URI Parameters
        DockerHelper.validateParameters(operation, parameters);

        Endpoint endpoint = new DockerEndpoint(uri, this, configuration);
        setProperties(configuration, parameters);
        configuration.setParameters(parameters);

        return endpoint;
    }

    protected DockerConfiguration getConfiguration() {
        if (configuration == null) {
            configuration = new DockerConfiguration();
        }

        return configuration;
    }

}
