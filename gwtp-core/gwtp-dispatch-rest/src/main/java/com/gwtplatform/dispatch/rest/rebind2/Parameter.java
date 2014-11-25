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

package com.gwtplatform.dispatch.rest.rebind2;

import com.google.gwt.core.ext.typeinfo.JParameter;

public class Parameter {
    private final JParameter parameter;
    private final String qualifiedSourceName;
    private final String variableName;

    public Parameter(
            JParameter parameter) {
        this(parameter, parameter.getType().getParameterizedQualifiedSourceName(), parameter.getName());
    }

    public Parameter(
            JParameter parameter,
            String qualifiedSourceName,
            String variableName) {
        this.parameter = parameter;
        this.qualifiedSourceName = qualifiedSourceName;
        this.variableName = variableName;
    }

    public JParameter getParameter() {
        return parameter;
    }

    public String getQualifiedSourceName() {
        return qualifiedSourceName;
    }

    public String getVariableName() {
        return variableName;
    }
}
