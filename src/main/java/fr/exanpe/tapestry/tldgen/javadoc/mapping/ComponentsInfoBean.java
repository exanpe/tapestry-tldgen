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
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Class describing the documentation of a library
 * 
 * @author lguerin
 */
@XStreamAlias("components")
public class ComponentsInfoBean
{
    /**
     * This field has to be instantiated twice :
     * 1. in constructor for writing
     * 2. in finalize for reading
     */
    @XStreamOmitField
    private Map<String, ComponentBean> components;

    /**
     * As we start from a collection, we need here an implicit collection
     * XStream doesn't support implicit collection on Map, so we duplicate the ComponentBean in a
     * List
     */
    @XStreamImplicit
    private List<ComponentBean> componentsList;

    public ComponentsInfoBean()
    {
        componentsList = new LinkedList<ComponentBean>();
    }

    public ComponentBean getComponentByClassName(String classname)
    {
        return components.get(classname);
    }

    /**
     * @return the components
     */
    public Map<String, ComponentBean> getComponents()
    {
        return components;
    }

    public void addComponent(ComponentBean component)
    {
        componentsList.add(component);
    }

    /**
     * Provides fast access to components
     */
    public void createFastAccess()
    {
        // provide non crashing business methods on fast access
        if (componentsList == null)
        {
            components = new HashMap<String, ComponentBean>();
            return;
        }

        Iterator<ComponentBean> ite = componentsList.iterator();

        components = new HashMap<String, ComponentBean>(componentsList.size() * 2);

        while (ite.hasNext())
        {
            ComponentBean next = ite.next();
            next.createFastAccess();
            components.put(next.getClassName(), next);
        }
    }

    public List<ComponentBean> getComponentsList()
    {
        return componentsList;
    }
}
