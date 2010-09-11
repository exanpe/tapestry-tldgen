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

import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import fr.exanpe.tapestry.tldgen.utils.TapestryTldGenConstants;

/**
 * Object mapping the main &lt;tag&gt; xml element
 * 
 * @author attonnnn
 */
@XStreamAlias("tag")
public class Tag
{
    @XStreamAlias("tag-class")
    private String tagClass;

    private String description;

    private String name;

    @XStreamAlias("body-content")
    private String bodyContent = TapestryTldGenConstants.BODY_CONTENT;

    @XStreamImplicit
    private List<Attribute> attributes;

    public Tag()
    {
        attributes = new LinkedList<Attribute>();
    }

    public String getTagClass()
    {
        return tagClass;
    }

    public void setTagClass(String tagClass)
    {
        this.tagClass = tagClass;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBodyContent()
    {
        return bodyContent;
    }

    public void setBodyContent(String bodyContent)
    {
        this.bodyContent = bodyContent;
    }

    public List<Attribute> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes)
    {
        this.attributes = attributes;
    }

}
