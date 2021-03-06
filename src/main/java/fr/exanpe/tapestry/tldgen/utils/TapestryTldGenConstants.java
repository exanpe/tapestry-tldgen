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

/**
 * This class contains the plugin constants
 * 
 * @author attonnnn
 */
public class TapestryTldGenConstants
{
    /**
     * Value for &lt;body-content&gt; generated element
     */
    public static final String BODY_CONTENT = "JSP";

    /**
     * Final TLD encoding
     */
    public static final String TLD_ENCODING = "UTF-8";

    /**
     * Header String for encoding
     */
    public static final String XML_HEADER_ENCODING = "<?xml version=\"1.0\" encoding=\"" + TLD_ENCODING + "\" ?>";

    /**
     * Text displaying a generated by comment
     */
    public static final String GENERATED_BY = "<!-- Generated by tapestry-tldgen - http://exanpe.free.fr/tapestry-tldgen/ -->";

    /**
     * xmlns for &lt;taglib&gt; tag
     */
    public static final String TAGLIB_XMLNS = "http://java.sun.com/xml/ns/javaee";

    /**
     * xmlns:xsi for &lt;taglib&gt; tag
     */
    public static final String TAGLIB_XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";

    /**
     * xsi:schemaLocation for &lt;taglib&gt; tag
     */
    public static final String TAGLIB_SCHEMA_LOC = "http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-jsptaglibrary_2_1.xsd";

    /**
     * Version for &lt;taglib&gt; tag
     */
    public static final String TAGLIB_VERSION = "2.1";

    /**
     * The Tapestry supported packages.
     */
    public final static String[] TAPESTRY_SUPPORTED_SUBPACKAGES = new String[]
    { "base", "components" };

    /**
     * Allow null String in description
     */
    public static final String ALLOW_NULL_TXT = "Allow null : ";

    /**
     * Default prefix in description
     */
    public static final String DEFAULT_PREFIX_TXT = "Default prefix : ";

    /**
     * Separator in tld for multiple information in a single tag
     */
    public static final String DEFAULT_TLD_SEPARATOR = " <br/> ";

    /**
     * Text displayed when no description has been found
     */
    public static final String NO_DESCRIPTION = "No description found";

}
