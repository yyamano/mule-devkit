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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.10.14 at 10:40:36 AM CDT 
//
package org.mule.devkit.model.studio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AttributeType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="AttributeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="alternativeTo" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="updater" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="xsdType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="caption" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="supportsExpressions" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="visibleInDialog" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="alwaysFill" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="controlled" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="topAnchor" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="versions" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="bottomAnchor" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="valuePersistence" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="customValidator" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="mode" type="{http://www.mulesoft.org/schema/mule/tooling.attributes}NewSimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributeType")
@XmlSeeAlso({
        NestedElementReference.class,
        TextType.class,
        EnumType.class,
        EncodingType.class,
        ExpressionAttributeType.class,
        IntegerType.class,
        ClassType.class,
        Booleantype.class,
        StringAttributeType.class,
        NewType.class,
        ModeType.class,
        FlowRefType.class,
        UrlType.class,
        PasswordType.class
})
public class AttributeType {

    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "alternativeTo")
    protected String alternativeTo;
    @XmlAttribute(name = "updater")
    protected String updater;
    @XmlAttribute(name = "xsdType")
    protected String xsdType;
    @XmlAttribute(name = "caption", required = true)
    protected String caption;
    @XmlAttribute(name = "description", required = true)
    protected String description;
    @XmlAttribute(name = "supportsExpressions")
    protected Boolean supportsExpressions;
    @XmlAttribute(name = "visibleInDialog")
    protected Boolean visibleInDialog;
    @XmlAttribute(name = "required")
    protected Boolean required;
    @XmlAttribute(name = "alwaysFill")
    protected Boolean alwaysFill;
    @XmlAttribute(name = "controlled")
    protected String controlled;
    @XmlAttribute(name = "topAnchor")
    protected String topAnchor;
    @XmlAttribute(name = "versions")
    protected String versions;
    @XmlAttribute(name = "bottomAnchor")
    protected String bottomAnchor;
    @XmlAttribute(name = "valuePersistence")
    protected String valuePersistence;
    @XmlAttribute(name = "customValidator")
    protected String customValidator;
    @XmlAttribute(name = "mode")
    protected NewSimpleType modeType;

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the alternativeTo property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAlternativeTo() {
        return alternativeTo;
    }

    /**
     * Sets the value of the alternativeTo property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAlternativeTo(String value) {
        this.alternativeTo = value;
    }

    /**
     * Gets the value of the updater property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getUpdater() {
        return updater;
    }

    /**
     * Sets the value of the updater property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUpdater(String value) {
        this.updater = value;
    }

    /**
     * Gets the value of the xsdType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getXsdType() {
        return xsdType;
    }

    /**
     * Sets the value of the xsdType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setXsdType(String value) {
        this.xsdType = value;
    }

    /**
     * Gets the value of the caption property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the value of the caption property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCaption(String value) {
        this.caption = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the supportsExpressions property.
     *
     * @return possible object is
     *         {@link Boolean }
     */
    public Boolean isSupportsExpressions() {
        return supportsExpressions;
    }

    /**
     * Sets the value of the supportsExpressions property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setSupportsExpressions(Boolean value) {
        this.supportsExpressions = value;
    }

    /**
     * Gets the value of the visibleInDialog property.
     *
     * @return possible object is
     *         {@link Boolean }
     */
    public Boolean isVisibleInDialog() {
        return visibleInDialog;
    }

    /**
     * Sets the value of the visibleInDialog property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setVisibleInDialog(Boolean value) {
        this.visibleInDialog = value;
    }

    /**
     * Gets the value of the required property.
     *
     * @return possible object is
     *         {@link Boolean }
     */
    public Boolean isRequired() {
        return required;
    }

    /**
     * Sets the value of the required property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setRequired(Boolean value) {
        this.required = value;
    }

    /**
     * Gets the value of the alwaysFill property.
     *
     * @return possible object is
     *         {@link Boolean }
     */
    public Boolean isAlwaysFill() {
        return alwaysFill;
    }

    /**
     * Sets the value of the alwaysFill property.
     *
     * @param value allowed object is
     *              {@link Boolean }
     */
    public void setAlwaysFill(Boolean value) {
        this.alwaysFill = value;
    }

    /**
     * Gets the value of the controlled property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getControlled() {
        return controlled;
    }

    /**
     * Sets the value of the controlled property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setControlled(String value) {
        this.controlled = value;
    }

    /**
     * Gets the value of the topAnchor property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTopAnchor() {
        return topAnchor;
    }

    /**
     * Sets the value of the topAnchor property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTopAnchor(String value) {
        this.topAnchor = value;
    }

    /**
     * Gets the value of the versions property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getVersions() {
        return versions;
    }

    /**
     * Sets the value of the versions property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVersions(String value) {
        this.versions = value;
    }

    /**
     * Gets the value of the bottomAnchor property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getBottomAnchor() {
        return bottomAnchor;
    }

    /**
     * Sets the value of the bottomAnchor property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBottomAnchor(String value) {
        this.bottomAnchor = value;
    }

    /**
     * Gets the value of the valuePersistence property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getValuePersistence() {
        return valuePersistence;
    }

    /**
     * Sets the value of the valuePersistence property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setValuePersistence(String value) {
        this.valuePersistence = value;
    }

    /**
     * Gets the value of the customValidator property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getCustomValidator() {
        return customValidator;
    }

    /**
     * Sets the value of the customValidator property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCustomValidator(String value) {
        this.customValidator = value;
    }

    /**
     * Gets the value of the modeType property.
     *
     * @return possible object is
     *         {@link NewSimpleType }
     */
    public NewSimpleType getModeType() {
        return modeType;
    }

    /**
     * Sets the value of the modeType property.
     *
     * @param value allowed object is
     *              {@link NewSimpleType }
     */
    public void setModeType(NewSimpleType value) {
        this.modeType = value;
    }

}
