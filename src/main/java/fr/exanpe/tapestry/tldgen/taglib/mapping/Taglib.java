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
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import fr.exanpe.tapestry.tldgen.utils.TapestryTldGenConstants;

/**
 * Object mapping the main &lt;taglib&gt; xml element
 * 
 * @author attonnnn
 */
@XStreamAlias("taglib")
public class Taglib
{
    @XStreamImplicit
    private List<Tag> tags;

    @XStreamAlias("tlib-version")
    private String taglibVersion;

    @XStreamAlias("short-name")
    private String shortName;

    private String uri;

    @XStreamAsAttribute
    private String xmlns = TapestryTldGenConstants.TAGLIB_XMLNS;

    @XStreamAlias("xmlns:xsi")
    @XStreamAsAttribute
    private String xmlnsXsi = TapestryTldGenConstants.TAGLIB_XMLNS_XSI;

    @XStreamAlias("xsi:schemaLocation")
    @XStreamAsAttribute
    private String schemaLocation = TapestryTldGenConstants.TAGLIB_SCHEMA_LOC;

    @XStreamAsAttribute
    private String version = TapestryTldGenConstants.TAGLIB_VERSION;

    public Taglib()
    {
        tags = new LinkedList<Tag>();
    }

    public List<Tag> getTags()
    {
        return tags;
    }

    public void setTags(List<Tag> tags)
    {
        this.tags = tags;
    }

    public String getTaglibVersion()
    {
        return taglibVersion;
    }

    public void setTaglibVersion(String taglibVersion)
    {
        this.taglibVersion = taglibVersion;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getXmlns()
    {
        return xmlns;
    }

    public void setXmlns(String xmlns)
    {
        this.xmlns = xmlns;
    }

    public String getXmlnsXsi()
    {
        return xmlnsXsi;
    }

    public void setXmlnsXsi(String xmlnsXsi)
    {
        this.xmlnsXsi = xmlnsXsi;
    }

    public String getSchemaLocation()
    {
        return schemaLocation;
    }

    public void setSchemaLocation(String schemaLocation)
    {
        this.schemaLocation = schemaLocation;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

}
