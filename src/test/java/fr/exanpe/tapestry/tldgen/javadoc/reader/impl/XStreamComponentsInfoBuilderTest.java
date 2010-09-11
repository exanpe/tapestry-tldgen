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
import java.io.StringReader;

import junit.framework.TestCase;
import fr.exanpe.tapestry.tldgen.javadoc.mapping.ComponentBean;
import fr.exanpe.tapestry.tldgen.javadoc.mapping.ComponentsInfoBean;
import fr.exanpe.tapestry.tldgen.javadoc.reader.ComponentsInfoBuilder;
import fr.exanpe.tapestry.tldgen.javadoc.reader.impl.XStreamComponentsInfoBuilder;

/**
 * Test class for {@link DigesterComponentsInfoBuilder}
 * 
 * @author lguerin
 */
public class XStreamComponentsInfoBuilderTest extends TestCase
{

    private static final String MOCK_COMP1_NAME = "fr.exanpe.contribustory.lib.components.InformationNote";
    private static final String MOCK_COMP2_NAME = "fr.exanpe.contribustory.lib.components.Tooltip";

    /**
     * Mock of XML result returned by Javadoc
     */
    private static final String MOCK_XML_OUTPUT = "<components>" + "<component className='" + MOCK_COMP1_NAME + "' superClassName='java.lang.Object'>"
    + "<description>Component displaying a bloc with information in it.</description>" + "<parameters><parameter name='id'><description>Must be provided to enable/"
    + "disable activation</description>" + "</parameter>" + "<parameter name='title'>" + "</parameter>" + "<parameter name='content'>" + "</parameter>"
    + "<parameter name='image'>" + "</parameter>" + "<parameter name='active'>" + "</parameter></parameters>" + "</component>" + "<component className='"
    + MOCK_COMP2_NAME + "' superClassName='java.lang.Object'>" + "<description>A Tooltip component.</description>" + "<parameters><parameter name='id'>Tooltip id"
    + "</parameter>" + "<parameter name='text'><description>Parameter used for getting tooltip text" + "</description></parameter>"
    + "<parameter name='title'><description>Parameter used for getting tooltip title" + "</description></parameter>"
    + "<parameter name='width'><description>The width in pixel of the tooltip box" + "</description></parameter></parameters>" + "</component>" + "</components>";

    /**
     * Number of components in mock
     */
    private static final int MOCK_NB_COMPONENTS = 2;

    /**
     * Number of parameters for comp1
     */
    private static final int MOCK_COMP1_NB_PARAMS = 5;

    /**
     * Number of parameters for comp2
     */
    private static final int MOCK_COMP2_NB_PARAMS = 4;

    /**
     * Builder
     */
    private final ComponentsInfoBuilder builder;

    /**
     * Unique constructor
     */
    public XStreamComponentsInfoBuilderTest()
    {
        builder = getBuilderInstance();
    }

    /**
     * @return builder instance
     */
    private ComponentsInfoBuilder getBuilderInstance()
    {
        return new XStreamComponentsInfoBuilder();
    }

    /**
     * Test method for
     * {@link fr.exanpe.tapestry.tldgen.builder.impl.DigesterComponentsInfoBuilder#readXMLOutput(java.lang.String)}
     * .
     */
    public void testReadXMLOutput()
    {
        Reader reader = new StringReader(MOCK_XML_OUTPUT);
        ComponentsInfoBean infos = null;

        infos = builder.readXMLOutput(reader);

        assertNotNull(infos);

        // Test number of components in the mock XML content
        assertEquals(MOCK_NB_COMPONENTS, infos.getComponents().size());

        // Test number of parameters
        ComponentBean comp1 = infos.getComponents().get(MOCK_COMP1_NAME);
        assertEquals(MOCK_COMP1_NB_PARAMS, comp1.getParameters().size());
        ComponentBean comp2 = infos.getComponents().get(MOCK_COMP2_NAME);
        assertEquals(MOCK_COMP2_NB_PARAMS, comp2.getParameters().size());

        // Test component description
        assertEquals("Component displaying a bloc with information in it.", comp1.getDescription());

        // Test parameter description
        assertEquals("Parameter used for getting tooltip text", comp2.getParameters().get("text").getDescription());

        // Test empty parameter description
        assertEquals(null, comp1.getParameters().get("image").getDescription());

        // Test parameter description split on two lines
        String firstLine = "Must be provided to enable/";
        assertTrue(comp1.getParameters().get("id").getDescription().length() > firstLine.length());
    }
}
