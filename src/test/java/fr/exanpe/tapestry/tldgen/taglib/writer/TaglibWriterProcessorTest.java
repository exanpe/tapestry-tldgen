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

package fr.exanpe.tapestry.tldgen.taglib.writer;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import junit.framework.TestCase;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Tag;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Taglib;

public class TaglibWriterProcessorTest extends TestCase
{

    public void testWriteTagLib() throws IOException
    {
        Writer writer = new StringWriter();
        TaglibWriterProcessor processor = new TaglibWriterProcessor(writer);
        
        Taglib taglib = new Taglib();
        taglib.setShortName("ShortName");
        taglib.setTaglibVersion("2.12");
        taglib.setUri("test.com");

        processor.write(taglib);
        
        String xmlRes = writer.toString();
        
        assertNotNull(xmlRes);
        assertNotSame("", xmlRes);
        
        assertTrue(xmlRes.contains("<taglib"));
        assertTrue(xmlRes.contains("<short-name>ShortName</short-name>"));
        assertTrue(xmlRes.contains("<tlib-version>2.12</tlib-version>"));
        assertTrue(xmlRes.contains("<uri>test.com</uri>"));
        assertTrue(xmlRes.contains("</taglib>"));
    }
    
    public void testWriteTag() throws IOException
    {
        Writer writer = new StringWriter();
        TaglibWriterProcessor processor = new TaglibWriterProcessor(writer);
        
        Taglib taglib = new Taglib();
        taglib.setShortName("ShortName");
        taglib.setTaglibVersion("2.12");
        taglib.setUri("test.com");
        
        
        Tag tag = new Tag();
        tag.setName("monTagName");
        tag.setTagClass("fr.MaClasse");
        tag.setDescription("Ma description");
        
        taglib.getTags().add(tag);
        
        processor.write(taglib);
        
        String xmlRes = writer.toString();
        
        assertNotNull(xmlRes);
        assertNotSame("", xmlRes);
        
        assertTrue(xmlRes.contains("<tag>"));
        assertTrue(xmlRes.contains("<name>monTagName</name>"));
        assertTrue(xmlRes.contains("<tag-class>fr.MaClasse</tag-class>"));
        assertTrue(xmlRes.contains("<description>Ma description</description>"));
        assertTrue(xmlRes.contains("</tag>"));
    }

}
