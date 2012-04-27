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

import java.io.File;

/**
 * <p>Abstract class that builds the UpdateSite elements</p>
 */
abstract class UpdateSiteElementsBuilder {
    protected static final String SEPARATOR = File.separator;


    protected String pluginVersion;
    protected String pluginName;
    protected String outputDirectory;
    protected String updateSitePath;
    protected String muleAppName;
    protected File classesDirectory;

    UpdateSiteElementsBuilder(String pluginName,
                              String pluginVersion,
                              String muleAppName,
                              String outputDirectory,
                              File classesDirectory) {

        this.pluginVersion = pluginVersion;
        this.pluginName = pluginName;
        this.muleAppName = muleAppName;
        this.outputDirectory = outputDirectory;
        this.classesDirectory = classesDirectory;
        this.updateSitePath = outputDirectory + SEPARATOR + "update-site" + SEPARATOR;

    }
    
    public abstract File build() throws MojoExecutionException;
}
