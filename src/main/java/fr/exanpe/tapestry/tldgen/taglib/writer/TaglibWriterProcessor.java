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
import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.FieldKeySorter;
import com.thoughtworks.xstream.converters.reflection.SortableFieldKeySorter;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

import fr.exanpe.tapestry.tldgen.taglib.mapping.Attribute;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Tag;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Taglib;
import fr.exanpe.tapestry.tldgen.utils.TapestryTldGenConstants;

/**
 * Write the taglib
 * 
 * @author attonnnn
 */
public class TaglibWriterProcessor
{
    /**
     * The writer used
     */
    private Writer writer;

    /**
     * Constructor
     * 
     * @param writer the writer to use
     */
    public TaglibWriterProcessor(Writer writer)
    {
        this.writer = writer;
    }

    /**
     * Sort the element in the output
     * 
     * @return the object sorting the elements
     */
    private FieldKeySorter getMappingSorter()
    {
        SortableFieldKeySorter sorter = new SortableFieldKeySorter();
        sorter.registerFieldOrder(Taglib.class, new String[]
        { "xmlns", "xmlnsXsi", "schemaLocation", "version", "taglibVersion", "shortName", "uri", "tags" });

        sorter.registerFieldOrder(Tag.class, new String[]
        { "description", "name", "tagClass", "bodyContent", "attributes" });

        sorter.registerFieldOrder(Attribute.class, new String[]
        { "description", "name", "required", "deferredValue", "parameterDescription", "fieldName"});

        return sorter;
    }

    /**
     * Write a taglib
     * 
     * @param taglib the taglib object to write
     * @throws IOException if any error occurs
     */
    public void write(Taglib taglib) throws IOException
    {
        PrettyPrintWriter ppwriter = null;

        try
        {
            XStream xstream = new XStream(new Sun14ReflectionProvider(new FieldDictionary(getMappingSorter())));
            xstream.setMode(XStream.NO_REFERENCES);
            xstream.autodetectAnnotations(true);

            writer.write(TapestryTldGenConstants.XML_HEADER_ENCODING);
            writer.write("\n");
            writer.write(TapestryTldGenConstants.GENERATED_BY);
            writer.write("\n");
            
            ppwriter = new PrettyPrintWriter(writer, PrettyPrintWriter.XML_1_1, new char[]
            { ' ', ' ' });

            xstream.marshal(taglib, ppwriter);

            ppwriter.flush();

        }
        finally
        {
            ppwriter.close();
            writer.close();
        }
    }
}
