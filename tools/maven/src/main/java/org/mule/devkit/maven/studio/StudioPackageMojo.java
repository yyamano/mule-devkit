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

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProjectHelper;
import org.eclipse.sisu.equinox.launching.internal.P2ApplicationLauncher;
import org.jfrog.maven.annomojo.annotations.*;
import org.mule.devkit.maven.AbstractMuleMojo;
import org.mule.util.IOUtils;
import sun.security.tools.JarSigner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Build a Mule plugin archive.
 */
@MojoPhase("package")
@MojoGoal("studio-package")
@MojoRequiresDependencyResolution("runtime")
public class StudioPackageMojo extends AbstractMuleMojo {


    
    @MojoComponent
    private MavenProjectHelper projectHelper;
    @MojoParameter(expression = "${project.build.outputDirectory}", required = true)
    private File classesDirectory;

    @MojoParameter(expression = "${keystore.path}")
    private String keystorePath;

    @MojoParameter(expression = "${alias}")
    private String alias;

    @MojoParameter(expression = "${licensePath}")
    private String licensePath;

    @MojoComponent
    private P2ApplicationLauncher launcher;


    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skipStudioPluginPackage) {
            return;
        }

        String pluginVersion = buildVersion();
        String pluginName = project.getArtifactId();

        File studioFeature = featureBuilderFor(pluginVersion, pluginName).build();
        File studioPlugin = pluginBuilderFor(pluginVersion, pluginName).build();
         new StudioSiteXmlBuilder(pluginName,pluginVersion,finalName,
                 outputDirectory.getPath(),
                 classesDirectory).build();

        sign(studioFeature, studioPlugin);

        createContentAndArtifacts();


        projectHelper.attachArtifact(project, "jar", "studio", studioPlugin);
        projectHelper.attachArtifact(project, "jar", "studio-feature", studioFeature);
    }

    private String buildVersion() {

        String projectVersion = project.getVersion();
        String cleanProjectVersion = projectVersion.replaceAll("[^0-9\\.]", "");

        String pluginVersion = getPluginVersionFrom(cleanProjectVersion);

        return pluginVersion + "."+buildQualifier();
    }

    private String getPluginVersionFrom(String cleanProjectVersion) {
        List<String> versionNumbers = new ArrayList<String>(Arrays.asList(cleanProjectVersion.split("\\.")));

        if ( versionNumbers.size() > 3 )
        {
            versionNumbers = versionNumbers.subList(0,3);
        }
        else
        {
            while (versionNumbers.size() < 3 )
            {
                versionNumbers.add("0");
            }
        }
        return StringUtils.join(versionNumbers.toArray(), ".");
    }

    private void sign(File studioFeature, File studioPlugin) {
        if ( keystorePath != null )
        {
            JarSigner jarsigner = new JarSigner();

            List<String> pluginOptions = buildOptions(studioPlugin.getPath());
            List<String> featureOptions = buildOptions(studioFeature.getPath());

            jarsigner.run(pluginOptions.toArray(new String[0]));
            jarsigner.run(featureOptions.toArray(new String[0]));
        }
    }

    private List<String> buildOptions(String path) {
        ArrayList<String> options = new ArrayList<String>();
        options.add("-keystore");
        options.add(keystorePath);
        options.add("-verbose");
        options.add(path);
        options.add(alias);

         return options;
    }


    private void createContentAndArtifacts() throws MojoExecutionException{
        try{
           String updateSitePath = outputDirectory + File.separator + "update-site" + File.separator;

           launcher.addArguments("-metadataRepository", "file:" + updateSitePath);
           launcher.addArguments("-source", updateSitePath);
           launcher.addArguments("-artifactRepository", "file:" + updateSitePath);
           launcher.addArguments("-publishArtifacts", "-append", "-compress");
           launcher.addArguments("-site",  "file:" + updateSitePath + "site.xml");
           launcher.setApplicationName("org.eclipse.equinox.p2.publisher.EclipseGenerator");

           launcher.execute(20);
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("error");
        }
      
    }


    private StudioPluginBuilder pluginBuilderFor(String pluginVersion, String pluginName) {
        return new StudioPluginBuilder(pluginName,
                pluginVersion,
                finalName,
                outputDirectory.getPath(),
                classesDirectory, project.getVersion());
    }

    private StudioFeatureBuilder featureBuilderFor(String pluginVersion, String pluginName) throws MojoExecutionException {

        try {
            String license = "";
            if ( licensePath != null )
            {
                license = IOUtils.toString(new FileInputStream(new File(licensePath)));
            }

            return new StudioFeatureBuilder(pluginName,
                    pluginVersion,
                    finalName,
                    outputDirectory.getPath(),
                    license,
                    classesDirectory);
            
        } catch (FileNotFoundException e) {
           throw new MojoExecutionException("Invalid license Path", e);
        }

        
    }


    private String buildQualifier() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.YEAR)) + calendar.get(Calendar.MONTH) + calendar.get(Calendar.DATE) +
                calendar.get(Calendar.HOUR) + calendar.get(Calendar.MINUTE);
    }


}
