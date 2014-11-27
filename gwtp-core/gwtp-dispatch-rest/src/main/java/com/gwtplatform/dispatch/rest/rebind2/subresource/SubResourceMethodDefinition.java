/**
 * Copyright 2014 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gwtplatform.dispatch.rest.rebind2.subresource;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.gwtplatform.dispatch.rest.rebind2.Parameter;
import com.gwtplatform.dispatch.rest.rebind2.resource.ResourceDefinition;
import com.gwtplatform.dispatch.rest.rebind2.resource.ResourceMethodDefinition;

public class SubResourceMethodDefinition extends ResourceMethodDefinition {
    private final List<ResourceDefinition> resourceDefinitions;

    public SubResourceMethodDefinition(
            JMethod method,
            List<Parameter> parameters,
            List<Parameter> inheritedParameters) {
        super(method, parameters, inheritedParameters);

        resourceDefinitions = Lists.newArrayList();
    }

    public void addResource(ResourceDefinition resourceDefinition) {
        resourceDefinitions.add(resourceDefinition);

        addImport(resourceDefinition.getQualifiedName());
    }

    public List<ResourceDefinition> getResourceDefinitions() {
        return Lists.newArrayList(resourceDefinitions);
    }
}