/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
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

package org.jboss.thunderlips.registry.extension;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.dmr.ModelNode;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;

/**
 *
 * @author Stan Silvert ssilvert@redhat.com (C) 2013 Red Hat Inc.
 */
public class AddConsoleHandler extends AbstractAddStepHandler {

    public static AddConsoleHandler INSTANCE = new AddConsoleHandler();

    private AddConsoleHandler() {}

    @Override
    protected void populateModel(ModelNode operation, ModelNode model) throws OperationFailedException {
        // TODO: localize exception. get id number
        if (!operation.get(OP).asString().equals(ADD)) {
            throw new OperationFailedException("Unexpected operation for add view. operation=" + operation.toString());
        }

        model.get("URL").set(operation.get("URL"));
    }
}
