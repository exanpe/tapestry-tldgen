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

/**
 * 
 */
package fr.exanpe.tapestry.tldgen.javadoc.mapping;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Class describing the documentation of a component
 * 
 * @author lguerin
 */
@XStreamAlias("component")
public class ComponentBean
{
    @XStreamAsAttribute
    private String superClassName;

    @XStreamAsAttribute
    private String className;

    private String description;

    @XStreamOmitField
    private Map<String, ParameterBean> parameters;

    /**
     * @see ComponentsInfoBean for explaination on duplication
     */
    @XStreamAlias("parameters")
    private List<ParameterBean> parametersList;

    public ComponentBean()
    {
        parametersList = new LinkedList<ParameterBean>();
    }

    public void addParameter(ParameterBean param)
    {
        parametersList.add(param);
    }

    public ParameterBean getParameterByName(String name)
    {
        return parameters.get(name);
    }

    /**
     * @return the superClassName
     */
    public String getSuperClassName()
    {
        return superClassName;
    }

    /**
     * @param superClassName the superClassName to set
     */
    public void setSuperClassName(String superClassName)
    {
        this.superClassName = superClassName;
    }

    /**
     * @return the className
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className)
    {
        this.className = className;
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

    /**
     * @return the parameters
     */
    public Map<String, ParameterBean> getParameters()
    {
        return parameters;
    }

    /**
     * Provides fast access to paramters
     */
    public void createFastAccess()
    {
        // provide non crashing business methods on fast access
        if (parametersList == null)
        {
            parameters = new HashMap<String, ParameterBean>();
            return;
        }

        Iterator<ParameterBean> ite = parametersList.iterator();

        parameters = new HashMap<String, ParameterBean>(parametersList.size() * 2);

        while (ite.hasNext())
        {
            ParameterBean next = ite.next();

            parameters.put(next.getName(), next);
        }

    }

    public List<ParameterBean> getParametersList()
    {
        return parametersList;
    }

}
