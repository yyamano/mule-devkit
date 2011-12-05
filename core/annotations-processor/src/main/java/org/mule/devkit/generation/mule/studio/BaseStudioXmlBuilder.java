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

package org.mule.devkit.generation.mule.studio;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.Transformer;
import org.mule.api.annotations.studio.InputGroup;
import org.mule.devkit.GeneratorContext;
import org.mule.devkit.generation.DevKitTypeElement;
import org.mule.devkit.model.studio.AttributeType;
import org.mule.devkit.model.studio.EnumElement;
import org.mule.devkit.model.studio.EnumType;
import org.mule.devkit.model.studio.Group;
import org.mule.devkit.model.studio.NestedElementReference;
import org.mule.devkit.model.studio.ObjectFactory;
import org.mule.devkit.utils.JavaDocUtils;
import org.mule.devkit.utils.NameUtils;
import org.mule.devkit.utils.TypeMirrorUtils;
import org.mule.util.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;
import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseStudioXmlBuilder {

    private static final String GENERAL_GROUP_NAME = "General";
    private static final String CONNECTION_GROUP_NAME = "Connection";
    protected ObjectFactory objectFactory;
    protected MuleStudioUtils helper;
    protected DevKitTypeElement typeElement;
    protected ExecutableElement executableElement;
    protected NameUtils nameUtils;
    protected JavaDocUtils javaDocUtils;
    protected TypeMirrorUtils typeMirrorUtils;
    protected Types typeUtils;
    protected String moduleName;
    protected GeneratorContext context;


    protected BaseStudioXmlBuilder(GeneratorContext context) {
        this.context = context;
        nameUtils = context.getNameUtils();
        javaDocUtils = context.getJavaDocUtils();
        typeMirrorUtils = context.getTypeMirrorUtils();
        typeUtils = context.getTypeUtils();
        helper = new MuleStudioUtils(context);
        objectFactory = new ObjectFactory();
    }

    protected BaseStudioXmlBuilder(GeneratorContext context, DevKitTypeElement typeElement) {
        this(context);
        this.typeElement = typeElement;
        moduleName = typeElement.name();
    }

    protected BaseStudioXmlBuilder(GeneratorContext context, ExecutableElement executableElement, DevKitTypeElement typeElement) {
        this(context, typeElement);
        this.executableElement = executableElement;
    }

    protected Collection<Group> processMethodParameters() {
        return processVariableElements(getParametersSorted());
    }

    protected Collection<Group> processConfigurableFields() {
        return processVariableElements(getConfigurableFieldsSorted());
    }

    private Collection<Group> processVariableElements(List<? extends VariableElement> variableElements) {

        Map<String, Group> groupsByName = new LinkedHashMap<String, Group>();

        if (typeElement.usesConnectionManager() && (executableElement == null || executableElement.getAnnotation(Transformer.class) == null)) {
            Group connectionAttributesGroup = new Group();
            connectionAttributesGroup.setCaption(helper.formatCaption(CONNECTION_GROUP_NAME));
            connectionAttributesGroup.setId(StringUtils.uncapitalize(CONNECTION_GROUP_NAME));
            groupsByName.put(CONNECTION_GROUP_NAME, connectionAttributesGroup);

            List<AttributeType> connectionAttributes = getConnectionAttributes(typeElement);
            connectionAttributesGroup.getRegexpOrEncodingOrModeSwitch().addAll(helper.createJAXBElements(connectionAttributes));
        }

        for (VariableElement parameter : variableElements) {
            JAXBElement<? extends AttributeType> jaxbElement = createJaxbElement(parameter);
            Group group = getOrCreateGroup(groupsByName, parameter);
            group.getRegexpOrEncodingOrModeSwitch().add(jaxbElement);
        }

        return groupsByName.values();
    }

    private Group getOrCreateGroup(Map<String, Group> groupsByName, VariableElement parameter) {
        InputGroup inputGroup = parameter.getAnnotation(InputGroup.class);
        if (inputGroup == null) {
            if (!groupsByName.containsKey(GENERAL_GROUP_NAME)) {
                Group groupGeneral = new Group();
                groupGeneral.setCaption(helper.formatCaption(GENERAL_GROUP_NAME));
                groupGeneral.setId(StringUtils.uncapitalize(GENERAL_GROUP_NAME));
                groupsByName.put(GENERAL_GROUP_NAME, groupGeneral);
            }
            return groupsByName.get(GENERAL_GROUP_NAME);
        } else {
            String groupName = inputGroup.value();
            if (!groupsByName.containsKey(groupName)) {
                Group group = new Group();
                group.setCaption(helper.formatCaption(groupName));
                group.setId(StringUtils.uncapitalize(groupName));
                groupsByName.put(groupName, group);
            }
            return groupsByName.get(groupName);
        }
    }

    private JAXBElement<? extends AttributeType> createJaxbElement(VariableElement parameter) {
        JAXBElement<? extends AttributeType> jaxbElement;
        if (typeMirrorUtils.isEnum(parameter)) {
            EnumType enumType = createEnumType(executableElement, parameter);
            jaxbElement = helper.createJAXBElement(enumType);
        } else if (typeMirrorUtils.isCollection(parameter)) {
            NestedElementReference childElement = createNestedElementReference(executableElement, parameter);
            jaxbElement = objectFactory.createGroupChildElement(childElement);
        } else {
            AttributeType attributeType = createAttributeType(executableElement, parameter);
            jaxbElement = helper.createJAXBElement(attributeType);
        }
        return jaxbElement;
    }

    private List<? extends VariableElement> getParametersSorted() {
        List<? extends VariableElement> parameters = new ArrayList<VariableElement>(executableElement.getParameters());
        Iterator<? extends VariableElement> iterator = parameters.iterator();
        while (iterator.hasNext()) {
            if (typeMirrorUtils.ignoreParameter(iterator.next())) {
                iterator.remove();
            }
        }
        Collections.sort(parameters, new VariableComparator(typeMirrorUtils));
        return parameters;
    }

    private AttributeType createAttributeType(ExecutableElement executableElement, VariableElement parameter) {
        AttributeType attributeType = helper.createAttributeTypeIgnoreEnumsAndCollections(parameter);
        if (attributeType != null) {
            helper.setAttributeTypeInfo(executableElement, parameter, attributeType);
        }
        return attributeType;
    }

    private List<AttributeType> getConnectionAttributes(DevKitTypeElement typeElement) {
        List<AttributeType> parameters = new ArrayList<AttributeType>();
        ExecutableElement connectMethod = typeElement.getMethodsAnnotatedWith(Connect.class).get(0);
        for (VariableElement connectAttributeType : connectMethod.getParameters()) {
            AttributeType parameter = helper.createAttributeTypeIgnoreEnumsAndCollections(connectAttributeType);
            helper.setAttributeTypeInfo(connectMethod, connectAttributeType, parameter);
            parameter.setRequired(false);
            parameters.add(parameter);
        }
        return parameters;
    }

    private NestedElementReference createNestedElementReference(ExecutableElement executableElement, VariableElement parameter) {
        NestedElementReference childElement = new NestedElementReference();
        String prefix;
        if(executableElement != null) {
            prefix = nameUtils.uncamel(executableElement.getSimpleName().toString());
            childElement.setDescription(helper.formatDescription(javaDocUtils.getParameterSummary(parameter.getSimpleName().toString(), executableElement)));
        } else {
            prefix = "configurable";
            childElement.setDescription(helper.formatDescription(javaDocUtils.getSummary(parameter)));
        }
        childElement.setName(MuleStudioXmlGenerator.URI_PREFIX + moduleName + '/' + prefix + '-' + nameUtils.uncamel(parameter.getSimpleName().toString()));
        childElement.setAllowMultiple(false);
        childElement.setCaption(helper.formatCaption(nameUtils.friendlyNameFromCamelCase(parameter.getSimpleName().toString())));
        childElement.setInplace(true);
        return childElement;
    }

    private EnumType createEnumType(ExecutableElement executableElement, VariableElement parameter) {
        EnumType enumType = new EnumType();
        enumType.setSupportsExpressions(true);
        enumType.setAllowsCustom(true);
        helper.setAttributeTypeInfo(executableElement, parameter, enumType);
        for (Element enumMember : typeUtils.asElement(parameter.asType()).getEnclosedElements()) {
            if (enumMember.getKind() == ElementKind.ENUM_CONSTANT) {
                String enumConstant = enumMember.getSimpleName().toString();
                EnumElement enumElement = new EnumElement();
                enumElement.setCaption(helper.formatCaption(javaDocUtils.getSummary(enumMember)));
                enumElement.setValue(enumConstant);
                enumType.getOption().add(enumElement);
            }
        }
        return enumType;
    }

    private List<VariableElement> getConfigurableFieldsSorted() {
        List<VariableElement> configurableFields = typeElement.getFieldsAnnotatedWith(Configurable.class);
        Collections.sort(configurableFields, new VariableComparator(typeMirrorUtils));
        return configurableFields;
    }
}