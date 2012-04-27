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
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.jar.JarArchiver;
import org.mule.devkit.generation.mule.studio.MuleStudioFeatureGenerator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/**
 * <p>Studio Feature builder. This is needed to publish the plugin in an Update Site.</p>
 *
 * <p>This class will generate a <plugin-name>-feature_<version>.qualifier.jar file in the target/update-site/features</p>
 *
 */
public class StudioFeatureBuilder extends UpdateSiteElementsBuilder {

    public static final String FEATURE_SUFIX = "-feature";

    private String path;
    private TokensReplacer tokensReplacer;
    

    StudioFeatureBuilder(String pluginName, 
                         String pluginVersion, 
                         String muleAppName, 
                         String outputDirectory,
                         String license,
                         File classesDirectory) {
        super(pluginName, pluginVersion, muleAppName, outputDirectory, classesDirectory);
        this.path = updateSitePath + "features";
        this.tokensReplacer = new TokensReplacer(buildTokens(pluginName, pluginVersion, license));
    }


    private Map<String, String> buildTokens(String pluginName, String pluginVersion, String license) {
        Map<String, String> tokens = new HashMap<String, String>();
        tokens.put("%FEATURE_ID%", pluginName + FEATURE_SUFIX);
        tokens.put("%PLUGIN_ID%", pluginName);
        tokens.put("%VERSION%", pluginVersion);
        tokens.put("%LICENSE%", license);

        return tokens;
    }

    @Override
    public File build() throws MojoExecutionException {
        File studioPlugin = new File(path, pluginName + FEATURE_SUFIX + "_" + pluginVersion +".jar");

        try {
            File file = new File(classesDirectory, MuleStudioFeatureGenerator.FEATURE_XML_FILENAME);

            
            if (!file.exists()) {
                throw new MojoExecutionException("Error while packaging Mule Studio plugin: " + file.getName() + " does not exist");
            }
            tokensReplacer.replaceTokensOn(file);

            JarArchiver archiver = new JarArchiver();
            archiver.addFile(file,file.getName());
            archiver.setDestFile(studioPlugin);

            try {
                studioPlugin.delete();
                archiver.createArchive();
            } catch (IOException e) {
                throw new MojoExecutionException("Error while packaging Studio plugin", e);
            }


            return studioPlugin;
        } catch (ArchiverException e) {
            throw new MojoExecutionException("Exception creating the Mule Plugin", e);
        }
    }
}
