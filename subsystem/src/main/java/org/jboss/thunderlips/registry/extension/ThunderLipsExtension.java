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

import java.util.Collections;
import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.List;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.thunderlips.registry.logging.ThunderLipsLogger;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

import org.jboss.as.controller.operations.common.Util;


/**
 * Main Extension class for the subsystem.
 * 
 * @author Stan Silvert ssilvert@redhat.com (C) 2013 Red Hat Inc.
 */
public class ThunderLipsExtension implements Extension {
    public static final String SUBSYSTEM_NAME = "thunderlips";
    public static final String NAMESPACE = "urn:jboss:domain:thunderlips:1.0";

    private static final ThunderLipsSubsystemParser PARSER = new ThunderLipsSubsystemParser();
    static final PathElement PATH_SUBSYSTEM = PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME);


    private static final String RESOURCE_NAME = ThunderLipsExtension.class.getPackage().getName() + ".LocalDescriptions";

    private static final int MANAGEMENT_API_MAJOR_VERSION = 1;
    private static final int MANAGEMENT_API_MINOR_VERSION = 0;
    private static final int MANAGEMENT_API_MICRO_VERSION = 0;

    protected static final PathElement SUBSYSTEM_PATH = PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME);

    private static final ResourceDefinition THUNDERLIPS_SUBSYSTEM_RESOURCE = new ThunderLipsSubsystemDefinition();

    static StandardResourceDescriptionResolver getResourceDescriptionResolver(final String... keyPrefix) {
        StringBuilder prefix = new StringBuilder(SUBSYSTEM_NAME);
        for (String kp : keyPrefix) {
            prefix.append('.').append(kp);
        }
        return new StandardResourceDescriptionResolver(prefix.toString(), RESOURCE_NAME, ThunderLipsExtension.class.getClassLoader(), true, false);
    }

    /** {@inheritDoc} */
    @Override
    public void initializeParsers(final ExtensionParsingContext context) {
        context.setSubsystemXmlMapping(SUBSYSTEM_NAME, ThunderLipsExtension.NAMESPACE, PARSER);
    }


    /** {@inheritDoc} */
    @Override
    public void initialize(final ExtensionContext context) {
        ThunderLipsLogger.ROOT_LOGGER.debug("Activating ThunderLips Extension");
        final SubsystemRegistration subsystem = context.registerSubsystem(SUBSYSTEM_NAME, MANAGEMENT_API_MAJOR_VERSION,
                MANAGEMENT_API_MINOR_VERSION, MANAGEMENT_API_MICRO_VERSION);
        ManagementResourceRegistration registration = subsystem.registerSubsystemModel(THUNDERLIPS_SUBSYSTEM_RESOURCE);
        registration.registerSubModel(new ConsoleDefinition());
        subsystem.registerXMLElementWriter(PARSER);
    }

    /**
     * The subsystem parser, which uses stax to read and write to and from xml
     */
    private static class ThunderLipsSubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>, XMLElementWriter<SubsystemMarshallingContext> {

        /** {@inheritDoc} */
        @Override
        public void readElement(final XMLExtendedStreamReader reader, final List<ModelNode> list) throws XMLStreamException {
            // Require no attributes
            ParseUtils.requireNoAttributes(reader);

            ModelNode addThunderLipsSub = Util.createAddOperation(PathAddress.pathAddress(PATH_SUBSYSTEM));
            list.add(addThunderLipsSub);

            while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                if (!reader.getLocalName().equals("console")) {
                    throw ParseUtils.unexpectedElement(reader);
                }

                readConsole(reader, list);

                while(reader.hasNext() && reader.nextTag() != END_ELEMENT) {
                    // move to next <view> element
                    // TODO: find a more proper way to do this
                }
            }
        }

        private void readConsole(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
            String name = null;
            String url = null;
            for (int i=0; i < reader.getAttributeCount(); i++) {
                String attr = reader.getAttributeLocalName(i);
                if (attr.equals("name")) {
                    name = reader.getAttributeValue(i);
                    continue;
                }

                if (attr.equals("URL")) {
                    url = reader.getAttributeValue(i);
                    continue;
                }

                throw ParseUtils.unexpectedAttribute(reader, i);
            }

            if (name == null) throw ParseUtils.missingRequired(reader, Collections.singleton("name"));
            if (url == null) throw ParseUtils.missingRequired(reader, Collections.singleton("URL"));

            ModelNode addConsole = new ModelNode();
            addConsole.get(OP).set(ADD);
            PathAddress addr = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME),
                                                       PathElement.pathElement("console", name));
            addConsole.get(OP_ADDR).set(addr.toModelNode());

            ModelNode descriptionNode = new ModelNode();
            descriptionNode.get("URL").set(url);

            addConsole.get("URL").set(url);

            list.add(addConsole);
        }

        /** {@inheritDoc} */
        @Override
        public void writeContent(final XMLExtendedStreamWriter writer, final SubsystemMarshallingContext context) throws XMLStreamException {
            context.startSubsystemElement(ThunderLipsExtension.NAMESPACE, false);
            writeConsoles(writer, context);
            writer.writeEndElement();
        }

        private void writeConsoles(XMLExtendedStreamWriter writer, SubsystemMarshallingContext context) throws XMLStreamException {
            if (!context.getModelNode().get("console").isDefined()) return;
            for (Property console : context.getModelNode().get("console").asPropertyList()) {
                writer.writeStartElement("console");
                writer.writeAttribute("name", console.getName());
                writer.writeAttribute("URL", console.getValue().get("URL").asString());
                writer.writeEndElement();
            }
        }
    }

}
