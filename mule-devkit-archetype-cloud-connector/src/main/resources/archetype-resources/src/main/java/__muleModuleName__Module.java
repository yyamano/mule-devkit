/**
 * This file was automatically generated by the Mule Development Kit
 */
#set($D='$')
#set($moduleNameLower = "${muleModuleName.toLowerCase()}")
#set($moduleGroupIdPath = $groupId.replace(".", "/"))
package ${muleModulePackage};

import org.mule.devkit.annotations.Module;
import org.mule.devkit.annotations.Configurable;
import org.mule.devkit.annotations.Processor;

@Module(name="${moduleNameLower}",
        namespace="http://repository.mulesoft.org/releases/${moduleGroupIdPath}/${artifactId}",
        schemaLocation="http://repository.mulesoft.org/releases/${moduleGroupIdPath}/${artifactId}/${version}/mule-${moduleNameLower}.xsd")
public class ${muleModuleName}Module
{
    @Configurable
    private String myProperty;

    public void setMyProperty(String myProperty)
    {
        this.myProperty = myProperty;
    }

    @Processor
    public String doSomething(String content)
    {
        /*
         * MESSAGE PROCESSOR CODE GOES HERE
         */

        return content;
    }
}
