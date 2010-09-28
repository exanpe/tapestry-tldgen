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

package fr.exanpe.tapestry.tldgen.taglib.builder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.SystemStreamLog;

import fr.exanpe.tapestry.tldgen.taglib.mapping.Attribute;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Tag;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Taglib;
import fr.exanpe.tapestry.tldgen.utils.TapestryTldGenConstants;
import fr.exanpe.test.heritage.components.SubSubComponent;

public class StructureBuilderTest extends TestCase
{
    public void testHeritage() throws MalformedURLException, MojoExecutionException
    {
        StructureBuilder builder = new StructureBuilder(new SystemStreamLog());

        Taglib t = builder.build("fr.exanpe.test.heritage", new String[]
        { "base", "components" }, new URL[]
        { new File("target/test-classes/").toURI().toURL() });

        assertNotNull(t);

        assertEquals(2, t.getTags().size());

        // on fait un test sur subsub
        if (t.getTags().get(0).getTagClass().equals(SubSubComponent.class.getName()))
            assertEquals(3, t.getTags().get(0).getAttributes().size());
        else
            assertEquals(3, t.getTags().get(1).getAttributes().size());

    }
    
    /**
     * Issue 4 Junit
     * @throws MalformedURLException
     * @throws MojoExecutionException
     */
    public void testParameterName() throws MalformedURLException, MojoExecutionException
    {
        StructureBuilder builder = new StructureBuilder(new SystemStreamLog());

        Taglib t = builder.build("fr.exanpe.test.heritage", new String[]
        { "base", "components" }, new URL[]
        { new File("target/test-classes/").toURI().toURL() });

        assertNotNull(t);

        assertEquals(2, t.getTags().size());

        // on fait un test sur subsub
        Tag subsubT = null;
        if (t.getTags().get(0).getTagClass().equals(SubSubComponent.class.getName())){
            subsubT = t.getTags().get(0);
        }else{
            subsubT = t.getTags().get(1);
        }
        
        boolean found = false;
        
        Iterator<Attribute> ite = subsubT.getAttributes().iterator();
        while(ite.hasNext()){
            Attribute a = ite.next();
            found |= (a.getName().equals("subnewname") && a.getFieldName().equals("subsubParam"));
        }
        
        assertTrue(found);
    }
    
    /**
     * Issue 1 Junit
     * @throws MalformedURLException
     * @throws MojoExecutionException
     */
    public void testParameterInfos() throws MalformedURLException, MojoExecutionException
    {
        StructureBuilder builder = new StructureBuilder(new SystemStreamLog());

        Taglib t = builder.build("fr.exanpe.test.heritage", new String[]
        { "base", "components" }, new URL[]
        { new File("target/test-classes/").toURI().toURL() });

        Attribute a = t.getTags().get(0).getAttributes().get(0);
        
        //just all to default
        assertTrue(a.getParameterDescription().contains(TapestryTldGenConstants.ALLOW_NULL_TXT+"true"));
        assertTrue(a.getParameterDescription().contains(TapestryTldGenConstants.DEFAULT_PREFIX_TXT+"prop"));
    }
}
