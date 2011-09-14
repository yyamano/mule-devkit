/**
 * Mule Development Kit
 * Copyright 2010-2011 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mule.devkit.it;

import org.mule.api.MuleContext;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.registry.RegistrationException;
import org.mule.construct.SimpleFlowConstruct;
import org.mule.devkit.it.config.OAuthModuleOAuth2Adapter;
import org.mule.tck.AbstractMuleTestCase;
import org.mule.tck.FunctionalTestCase;
import org.mule.transport.http.HttpConnector;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class OAuthModuleTest extends FunctionalTestCase {

    @Override
    protected String getConfigResources() {
        return "oauth.xml";
    }

    @Override
    protected MuleContext createMuleContext() throws Exception {
        MuleContext muleContext = super.createMuleContext();
        muleContext.getRegistry().registerObject("connector.http.mule.default", new HttpConnector(muleContext));
        return muleContext;
    }

    public void testNonProtectedResource() throws Exception {
        MuleEvent responseEvent = runFlow("nonProtectedResource");
        assertEquals(OAuthModule.NON_PROTECTED_RESOURCE, responseEvent.getMessageAsString());
    }

    public void testProtectedResource() throws Exception {
        MuleEvent responseEvent = runFlow("protectedResource");
        String url = verifyUserIsRedirectedToAuthorizationUrl(responseEvent);
        simulateCallbackUponUserAuthorizingConsumer(url);
        responseEvent = runFlow("protectedResource");
        verifiyProtectedResourceWasAccessed(responseEvent);
    }

    public void testProtectedResourceAccessTokenExpired() throws Exception {
        MuleEvent responseEvent = runFlow("protectedResource");
        String url = verifyUserIsRedirectedToAuthorizationUrl(responseEvent);
        simulateCallbackUponUserAuthorizingConsumer(url);
        responseEvent = runFlow("protectedResource");
        verifiyProtectedResourceWasAccessed(responseEvent);

        overrideTokenExpiration();

        responseEvent = runFlow("protectedResource");
        url = verifyUserIsRedirectedToAuthorizationUrl(responseEvent);
        simulateCallbackUponUserAuthorizingConsumer(url);
        responseEvent = runFlow("protectedResource");
        verifiyProtectedResourceWasAccessed(responseEvent);
    }

    private void overrideTokenExpiration() throws RegistrationException {
        OAuthModuleOAuth2Adapter oauthAdapter = muleContext.getRegistry().lookupObject(OAuthModuleOAuth2Adapter.class);
        oauthAdapter.setExpiration(new Date(System.currentTimeMillis() - 1000));
    }

    private void verifiyProtectedResourceWasAccessed(MuleEvent responseEvent) throws MuleException {
        assertEquals(OAuthModule.PROTECTED_RESOURCE, responseEvent.getMessageAsString());
    }

    private void simulateCallbackUponUserAuthorizingConsumer(String url) throws IOException {
        String callbackUrl = url.substring(url.indexOf("redirect_uri=") + "redirect_uri=".length()).replaceAll("%3A", ":").replaceAll("%2F", "/");
        callbackUrl += "?code=" + Constants.OAUTH_VERIFIER;
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(callbackUrl).openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.getInputStream();
    }

    private String verifyUserIsRedirectedToAuthorizationUrl(MuleEvent responseEvent) {
        assertEquals("302", responseEvent.getMessage().getOutboundProperty("http.status"));
        String url = responseEvent.getMessage().getOutboundProperty("Location");
        assertNotNull(url);
        return url;
    }

    private SimpleFlowConstruct lookupFlowConstruct(String name) {
        return (SimpleFlowConstruct) AbstractMuleTestCase.muleContext.getRegistry().lookupFlowConstruct(name);
    }

    private MuleEvent runFlow(String flowName) throws Exception {
        SimpleFlowConstruct flow = lookupFlowConstruct(flowName);
        MuleEvent event = AbstractMuleTestCase.getTestEvent("");
        return flow.process(event);
    }
}