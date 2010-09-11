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

package fr.exanpe.tapestry.tldgen.javadoc.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.exanpe.tapestry.tldgen.javadoc.mapping.ComponentBean;
import fr.exanpe.tapestry.tldgen.javadoc.mapping.ComponentsInfoBean;
import fr.exanpe.tapestry.tldgen.javadoc.mapping.ParameterBean;

/**
 * Writer using the base DOM API
 * THis choice has been made to bypass classloader problems with javadoc command
 * The file written will be in the encoding of the source files
 * 
 * @author jmaupoux
 */
public class JavadocTmpFileWriter
{
    private Writer writer;

    private String sourceEncoding;

    /**
     * @param writer the writer to write the javadoc elements
     * @param sourceEncoding the source files encoding
     */
    public JavadocTmpFileWriter(Writer writer, String sourceEncoding)
    {
        this.writer = writer;
        this.sourceEncoding = sourceEncoding;
    }

    /**
     * Write the element into the given writer
     * 
     * @param infos the javadoc information about the components
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public void write(ComponentsInfoBean infos) throws IOException, ParserConfigurationException, TransformerException
    {

        try
        {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element root = doc.createElement("components");
            doc.appendChild(root);

            writeElements(doc, root, infos);

            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty(OutputKeys.ENCODING, sourceEncoding);

            StreamResult result = new StreamResult(writer);

            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
        }
        finally
        {
            writer.close();
        }
    }

    private void writeElements(Document doc, Element root, ComponentsInfoBean infos)
    {
        Iterator<ComponentBean> comps = infos.getComponentsList().iterator();

        while (comps.hasNext())
        {
            Element el = getComponentElement(doc, comps.next());
            root.appendChild(el);
        }

    }

    private Element getComponentElement(Document doc, ComponentBean comp)
    {
        Element compEl = doc.createElement("component");
        compEl.setAttribute("className", comp.getClassName());
        compEl.setAttribute("superClassName", comp.getSuperClassName());

        Element descrEl = doc.createElement("description");
        descrEl.setTextContent(comp.getDescription());
        compEl.appendChild(descrEl);

        Element parametersEl = getParametersElement(doc, comp);

        compEl.appendChild(parametersEl);

        return compEl;
    }

    private Element getParametersElement(Document doc, ComponentBean comp)
    {
        Iterator<ParameterBean> params = comp.getParametersList().iterator();

        Element parametersEl = doc.createElement("parameters");

        while (params.hasNext())
        {
            Element paramEl = getParameterElement(doc, params.next());
            parametersEl.appendChild(paramEl);
        }

        return parametersEl;
    }

    private Element getParameterElement(Document doc, ParameterBean param)
    {
        Element paramEl = doc.createElement("parameter");
        paramEl.setAttribute("name", param.getName());

        Element descrEl = doc.createElement("description");
        descrEl.setTextContent(param.getDescription());
        paramEl.appendChild(descrEl);

        return paramEl;
    }
}
