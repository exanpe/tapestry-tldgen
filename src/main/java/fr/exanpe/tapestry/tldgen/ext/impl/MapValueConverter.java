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

package fr.exanpe.tapestry.tldgen.ext.impl;

import java.util.Collection;
import java.util.Map;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * Convert the values of a Map
 * Not used anymore as xstream doesnt work with @XStreamImplicit on Map (only on Collection)
 * This class remains in case XStream implements the functionality
 * 
 * @author attonnnn
 */
public class MapValueConverter extends AbstractCollectionConverter
{

    public MapValueConverter(Mapper mapper)
    {
        super(mapper);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context)
    {
        Collection<Object> os = ((Map) source).values();

        for (Object o : os)
        {
            System.out.println("write :" + os);
            writeItem(o, context, writer);
        }
        System.out.println("end");
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
    {
        throw new IllegalStateException("not implemented as no unmarshalling must occur");
    }

    public boolean canConvert(Class type)
    {
        return Map.class.isAssignableFrom(type);
    }
}
