/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package org.jboss.thunderlips.idp;

import javax.servlet.http.HttpSession;
import org.picketlink.common.exceptions.ConfigurationException;
import org.picketlink.common.exceptions.ProcessingException;
import org.picketlink.config.federation.IDPType;
import org.picketlink.config.federation.ProviderType;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2HandlerChainConfig;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2HandlerConfig;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2HandlerRequest;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2HandlerResponse;
import org.picketlink.identity.federation.web.core.HTTPContext;
import org.picketlink.identity.federation.web.handlers.saml2.SAML2AttributeHandler;

/**
 *
 * @author Stan Silvert ssilvert@redhat.com (C) 2013 Red Hat Inc.
 */
public class ThunderlipsAttributeHandler extends SAML2AttributeHandler {

    @Override
    public void initHandlerConfig(SAML2HandlerConfig handlerConfig) throws ConfigurationException {
        System.out.println("****************");
        System.out.println("Called initHandlerConfig");
        System.out.println("****************");
        super.initHandlerConfig(handlerConfig);
    }

    @Override
    public void initChainConfig(SAML2HandlerChainConfig handlerChainConfig) throws ConfigurationException {
        System.out.println("****************");
        System.out.println("Called initChainConfig");
        System.out.println("****************");

        super.initChainConfig(handlerChainConfig);
    }

    @Override
    protected void handleIDPResponse(SAML2HandlerRequest request) {
        System.out.println("****************");
        System.out.println("Called handleIDPResponse");
        System.out.println("****************");
        super.handleIDPResponse(request);
    }

    @Override
    public HANDLER_TYPE getType() {
        System.out.println("****************");
        System.out.println("Called getType");
        System.out.println("****************");
        return super.getType();
    }

    @Override
    public void reset() throws ProcessingException {
        System.out.println("****************");
        System.out.println("Called reset");
        System.out.println("****************");
        super.reset();
    }

    @Override
    public void generateSAMLRequest(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
        System.out.println("****************");
        System.out.println("Called generateSAMLRequest");
        System.out.println("****************");
        super.generateSAMLRequest(request, response);
    }

    @Override
    protected ProviderType getProviderconfig() {
        System.out.println("****************");
        System.out.println("Called getProviderconfig");
        System.out.println("****************");
        return super.getProviderconfig();
    }

    @Override
    public void handleRequestType(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
        System.out.println("****************");
        System.out.println("Called handleRequestType");
        System.out.println("****************");

        super.handleRequestType(request, response);
    }

    @Override
    public void handleStatusResponseType(SAML2HandlerRequest request, SAML2HandlerResponse response) throws ProcessingException {
        System.out.println("****************");
        System.out.println("Called handleStatusResponseType");
        System.out.println("****************");
        super.handleStatusResponseType(request, response); 
    }
}
