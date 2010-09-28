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

package fr.exanpe.tapestry.tldgen.utils;

import fr.exanpe.tapestry.tldgen.javadoc.mapping.ComponentBean;
import fr.exanpe.tapestry.tldgen.javadoc.mapping.ComponentsInfoBean;
import fr.exanpe.tapestry.tldgen.javadoc.mapping.ParameterBean;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Attribute;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Tag;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Taglib;

/**
 * Merge the information collected through annotations and Javadoc into the final structure
 * 
 * @author attonnnn
 */
public class JavadocBeanMerger
{
    /**
     * Merge the information collected through annotations and Javadoc into the final structure
     * 
     * @param taglib the taglib structure
     * @param infos the Javadoc structure
     */
    public static void mergeToTaglib(Taglib taglib, ComponentsInfoBean infos)
    {

        for (Tag t : taglib.getTags())
        {
            ComponentBean comp = infos.getComponentByClassName(t.getTagClass());

            // infos have been collected through Javadoc
            if (comp != null)
            {
                t.setDescription(comp.getDescription());

                for (Attribute a : t.getAttributes())
                {

                    ParameterBean param = comp.getParameterByName(a.getName());

                    String description = null;
                    
                    if (param != null)
                    {
                        description = param.getDescription();
                    }
                    else
                    {
                        description = getParentAttributeDescription(comp, a, infos);
                    }
                    
                    a.setDescription(description+"\n"+a.getParameterDescription());
                }
            }
        }
    }

    /**
     * Recursively search the Javadoc into parent classes
     * Only manage the classes which the source files are in the project.
     * Stops when {@link Object} class is found
     * 
     * @param comp the "Javadoc" object
     * @param a the Attribute searched
     * @param infos the main Javadoc structure
     * @return a String corresponding to the Javadoc documentation, or null is not found
     */
    private static String getParentAttributeDescription(ComponentBean comp, Attribute a, ComponentsInfoBean infos)
    {
        try
        {
            Class<?> c = Class.forName(comp.getClassName());

            if (c.getSuperclass() == null) { return null; }

            comp = infos.getComponentByClassName(c.getSuperclass().getName());

            if (comp != null)
            {
                ParameterBean superParam = comp.getParameterByName(a.getName());

                if (superParam != null)
                {
                    return superParam.getDescription();
                }
                else
                {
                    return getParentAttributeDescription(comp, a, infos);
                }
            }

            // else pas enregistré donc pas dans les sources donc on quitte

        }
        catch (ClassNotFoundException e)
        {
            // should not happen
            // just return null is ok
        }

        return null;
    }
}
