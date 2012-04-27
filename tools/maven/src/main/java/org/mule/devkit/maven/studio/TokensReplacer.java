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
import org.mule.util.IOUtils;

import java.io.*;
import java.util.Map;

/**
 * <p>Replace a map of tokens in a file</p>
 *
 * <p>This is used by the Site Update builders to replace the tokens added by the Generators</p>
 */
class TokensReplacer {
    
    private Map<String, String> tokens;

    public TokensReplacer(Map<String, String> tokens) {
        this.tokens = tokens;
    }

    public void replaceTokensOn(File file) throws MojoExecutionException {
        try {
            String fileContents = IOUtils.toString(new FileInputStream(file));
            
            for ( String token : tokens.keySet() )
            {
                fileContents = fileContents.replaceAll(token, tokens.get(token));
            }
            IOUtils.copy(new StringReader(fileContents), new FileOutputStream(file), "UTF-8");
        } catch (IOException e) {
            throw new MojoExecutionException("Error replacing tokens in file: " + file.getAbsolutePath(), e);
        }
    }
}
