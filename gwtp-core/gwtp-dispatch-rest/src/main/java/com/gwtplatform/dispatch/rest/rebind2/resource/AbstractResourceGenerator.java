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

package com.gwtplatform.dispatch.rest.rebind2.resource;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.app.VelocityEngine;

import com.google.common.collect.Sets;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.gwtplatform.dispatch.rest.rebind2.AbstractVelocityGenerator;
import com.gwtplatform.dispatch.rest.rebind2.utils.Arrays;
import com.gwtplatform.dispatch.rest.rebind2.utils.Logger;

import static com.gwtplatform.dispatch.rest.rebind2.utils.Generators.findGenerator;
import static com.gwtplatform.dispatch.rest.rebind2.utils.Generators.getGenerator;

public abstract class AbstractResourceGenerator extends AbstractVelocityGenerator implements ResourceGenerator {
    private final Set<ResourceMethodGenerator> resourceMethodGenerators;

    private Set<String> imports;
    private ResourceContext resourceContext;

    protected AbstractResourceGenerator(
            Logger logger,
            GeneratorContext context,
            VelocityEngine velocityEngine, Set<ResourceMethodGenerator> resourceMethodGenerators) {
        super(logger, context, velocityEngine);

        this.resourceMethodGenerators = resourceMethodGenerators;
    }

    @Override
    public boolean canGenerate(ResourceContext resourceContext) throws UnableToCompleteException {
        return canGenerateAllMethods();
    }

    @Override
    public ResourceDefinition generate(ResourceContext context) throws UnableToCompleteException {
        setContext(context);
        imports = Sets.newTreeSet();

        PrintWriter printWriter = tryCreate();
        if (printWriter != null) {
            imports.add(getResourceType().getQualifiedSourceName());

            generateMethods();

            mergeTemplate(printWriter);
            commit(printWriter);
        }

        return getResourceDefinition();
    }

    @Override
    protected void populateTemplateVariables(Map<String, Object> variables) {
        variables.put("imports", imports);
        variables.put("resourceType", getResourceType().getSimpleSourceName());
        variables.put("methods", getResourceDefinition().getMethodDefinitions());
    }

    @Override
    protected String getPackageName() {
        return getResourceType().getPackage().getName();
    }

    protected void setContext(ResourceContext resourceContext) {
        this.resourceContext = resourceContext;
    }

    protected ResourceContext getResourceContext() {
        return resourceContext;
    }

    protected JClassType getResourceType() {
        return resourceContext.getResourceType();
    }

    protected void generateMethods() throws UnableToCompleteException {
        List<JMethod> methods = Arrays.asList(getResourceType().getInheritableMethods());

        for (JMethod enclosedMethod : methods) {
            generateMethod(enclosedMethod);
        }
    }

    protected void generateMethod(JMethod method) throws UnableToCompleteException {
        ResourceMethodContext methodContext =
                new ResourceMethodContext(getResourceDefinition(), getResourceContext(), method);
        ResourceMethodGenerator generator =
                getGenerator(getLogger(), resourceMethodGenerators, methodContext);
        ResourceMethodDefinition methodDefinition = generator.generate(methodContext);

        getResourceDefinition().addMethodDefinition(methodDefinition);
        imports.addAll(methodDefinition.getImports());
    }

    protected abstract ResourceDefinition getResourceDefinition();

    private boolean canGenerateAllMethods() {
        List<JMethod> methods = Arrays.asList(getResourceType().getInheritableMethods());
        boolean canGenerate = true;

        for (JMethod enclosedMethod : methods) {
            ResourceMethodContext methodContext = new ResourceMethodContext(null, getResourceContext(), enclosedMethod);
            ResourceMethodGenerator generator = findGenerator(getLogger(), resourceMethodGenerators, methodContext);

            if (generator == null) {
                canGenerate = false;
                getLogger().debug("Cannot find a resource method generator for `%s#%s`.",
                        getResourceType().getQualifiedSourceName(), enclosedMethod.getName());
            }
        }

        return canGenerate;
    }
}