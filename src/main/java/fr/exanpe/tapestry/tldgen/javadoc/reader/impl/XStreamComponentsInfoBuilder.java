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
package fr.exanpe.tapestry.tldgen.javadoc.reader.impl;

import java.io.Reader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fr.exanpe.tapestry.tldgen.javadoc.mapping.ComponentBean;
import fr.exanpe.tapestry.tldgen.javadoc.mapping.ComponentsInfoBean;
import fr.exanpe.tapestry.tldgen.javadoc.mapping.ParameterBean;
import fr.exanpe.tapestry.tldgen.javadoc.reader.ComponentsInfoBuilder;

/**
 * XStream implementation of {@link ComponentsInfoBuilder}
 * 
 * @author attonnnn
 */
public class XStreamComponentsInfoBuilder implements ComponentsInfoBuilder
{

    /*
     * (non-Javadoc)
     * @see fr.exanpe.tapestry.tldgen.builder.ComponentsInfoBuilder#readXMLOutput(java.lang.String)
     */
    public ComponentsInfoBean readXMLOutput(Reader reader)
    {
        // uses the encoding declared in the XML file
        XStream xstream = new XStream(new DomDriver());

        // does not work on 'fromXML' !
        // xstream.autodetectAnnotations(true);

        xstream.processAnnotations(ComponentsInfoBean.class);
        xstream.processAnnotations(ComponentBean.class);
        xstream.processAnnotations(ParameterBean.class);

        ComponentsInfoBean bean = (ComponentsInfoBean) xstream.fromXML(reader);
        // init maps
        bean.createFastAccess();

        return bean;
    }

}
