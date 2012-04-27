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
import org.mule.devkit.generation.mule.studio.MuleStudioIconsGenerator;
import org.mule.devkit.generation.mule.studio.MuleStudioPluginGenerator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Studio plugin jar file builder</p>
 *
 * <p>This class generates a <plugin-name>_version.qualifier.jar in the target/update-site/plugins folder</p>
 */
class StudioPluginBuilder extends UpdateSiteElementsBuilder {
    
    private String path;
    private TokensReplacer tokensReplacer;
    
    StudioPluginBuilder(String pluginName, String pluginVersion, String muleAppName, String outputDirectory, File classesDirectory, String projectVersion) {
        super(pluginName, pluginVersion, muleAppName, outputDirectory, classesDirectory);
        this.path = updateSitePath + "plugins";
        this.tokensReplacer = new TokensReplacer(buildTokens(pluginName, pluginVersion, muleAppName, projectVersion));
    }

    private Map<String, String> buildTokens(String pluginName, String pluginVersion, String muleAppName, String projectVersion) {
        Map<String, String> tokens = new HashMap<String, String>();
        tokens.put("%JAR_NAME%", muleAppName + ".jar");
        tokens.put("%ZIP_NAME%", muleAppName + ".zip");
        tokens.put("%BUNDLE_NAME%", pluginName);
        tokens.put("%PROJECT_VERSION%", projectVersion);
        tokens.put("%VERSION%", pluginVersion);
        tokens.put("%SOURCES_JAR%", muleAppName + "-sources.jar");
        tokens.put("%JAVADOC_JAR%", muleAppName + "-javadoc.jar");

        return tokens;
    }


    public File build() throws MojoExecutionException {

        try {
            return createStudioPlugin();
        } catch (ArchiverException e) {
            throw new MojoExecutionException("Exception creating the Mule Plugin", e);
        }

    }

    private File createStudioPlugin() throws MojoExecutionException, ArchiverException {
        File studioPlugin = new File(path, pluginName + "_" + pluginVersion +".jar");

        JarArchiver archiver = new JarArchiver();

        archiveFile(archiver, muleAppName + ".zip");

        addArchivedClasses(archiver);

        for (String fileName : MuleStudioPluginGenerator.GENERATED_FILES) {
            File file = new File(classesDirectory, fileName);

            if (!file.exists()) {
                throw new MojoExecutionException("Error while packaging Mule Studio plugin: " + file.getName() + " does not exist");
            }
            if (fileName.endsWith(".xml") ) {
                tokensReplacer.replaceTokensOn(file);
            }

            if ( isManifest(fileName) )
            {
                tokensReplacer.replaceTokensOn(file);
                archiver.setManifest(file);
            }
            else
            {
                archiver.addFile(file, file.getPath().substring(file.getPath().indexOf(classesDirectory.getPath()) + classesDirectory.getPath().length() + 1));
            }
        }

        archiver.addDirectory(new File(classesDirectory, MuleStudioIconsGenerator.ICONS_FOLDER), MuleStudioIconsGenerator.ICONS_FOLDER, null, null);
        archiver.setDestFile(studioPlugin);


        try {
            studioPlugin.delete();
            archiver.createArchive();
        } catch (IOException e) {
            throw new MojoExecutionException("Error while packaging Studio plugin", e);
        }

        return studioPlugin;
    }

    private boolean isManifest(String fileName) {
        return fileName.endsWith("MF");
    }

    private void archiveFile(JarArchiver archiver, String fileName) throws MojoExecutionException {
        File muleZipFile = new File(outputDirectory, fileName);

        if ( !muleZipFile.exists() ) {

            throw new MojoExecutionException(fileName +" does not exist" );
        }

        archiver.addFile(muleZipFile, muleZipFile.getName());
    }



    private void addArchivedClasses(JarArchiver archiver) throws ArchiverException, MojoExecutionException {
        if (!classesDirectory.exists()) {
            return;
        }

        final JarArchiver jarArchiver = new JarArchiver();

        jarArchiver.addDirectory(classesDirectory, null, null);
        final File jar = new File(outputDirectory, muleAppName + ".jar");
        jarArchiver.setDestFile(jar);
        try {
            jarArchiver.createArchive();

            archiver.addFile(jar, jar.getName());
        } catch (IOException e) {
            final String message = "Cannot create project jar";
            throw new MojoExecutionException(message, e);
        }
    }
}

