//
// Copyright 2010 EXANPE <exanpe@gmail.com>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package fr.exanpe.tapestry.tldgen.taglib.mapping;

import org.apache.tapestry5.annotations.Parameter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import fr.exanpe.tapestry.tldgen.ext.impl.DeferredValueConverter;

/**
 * Object mapping the main &lt;attribute&gt; xml element
 * 
 * @author attonnnn
 */
@XStreamAlias("attribute")
public class Attribute
{    
    /**
     * Name of the parameter as defined by the user
     * @see Parameter#name()
     */
    private String name;

    private String description;
    
    /**
     * A annotation specific description used to gather information from @Parameter annotation.
     */
    @XStreamOmitField
    private String parameterDescription;

    private boolean required;

    @XStreamConverter(DeferredValueConverter.class)
    @XStreamAlias("deferred-value")
    private String deferredValue;

    public Attribute()
    {
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public String getParameterDescription()
    {
        return parameterDescription;
    }

    public void setParameterDescription(String parameterDescription)
    {
        this.parameterDescription = parameterDescription;
    }

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public String getDeferredValue()
    {
        return this.deferredValue;
    }

    public void setDeferredValue(String type)
    {
        this.deferredValue = type;
    }

    
}
