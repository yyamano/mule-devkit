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

package org.mule.devkit.maven.studio;

import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: fernandofederico
 * Date: 4/17/12
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudioSiteXmlBuilder extends UpdateSiteElementsBuilder
{
    StudioSiteXmlBuilder(String pluginName, String pluginVersion, String muleAppName, String outputDirectory, File classesDirectory) {
        super(pluginName, pluginVersion, muleAppName, outputDirectory, classesDirectory);
    }

    @Override
    public File build() throws MojoExecutionException {
        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element site = document.createElement("site");
            document.appendChild(site);

            Element feature = document.createElement("feature");
            feature.setAttribute("url", "features" + SEPARATOR + pluginName + StudioFeatureBuilder.FEATURE_SUFIX + "_" + pluginVersion +".jar");
            feature.setAttribute("id", pluginName + StudioFeatureBuilder.FEATURE_SUFIX );
            feature.setAttribute("version", pluginVersion );

            Element category = document.createElement("category");
            category.setAttribute("name", "connector");

            feature.appendChild(category);

            Element categoryDef = document.createElement("category-def");
            categoryDef.setAttribute("name", "connector");
            categoryDef.setAttribute("label", "connector");
            site.appendChild(feature);
            site.appendChild(categoryDef);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);
            File siteXml = new File(updateSitePath, "site.xml");
            StreamResult result = new StreamResult(siteXml);
            transformer.transform(source, result);

            return siteXml;
        } catch (Exception e) {
            throw new MojoExecutionException("Could not create site.xml file",e);
        }
    }
}
