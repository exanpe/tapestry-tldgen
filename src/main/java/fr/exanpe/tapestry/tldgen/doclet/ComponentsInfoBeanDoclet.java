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

package fr.exanpe.tapestry.tldgen.doclet;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.LanguageVersion;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.AnnotationDesc.ElementValuePair;

import fr.exanpe.tapestry.tldgen.javadoc.mapping.ComponentBean;
import fr.exanpe.tapestry.tldgen.javadoc.mapping.ComponentsInfoBean;
import fr.exanpe.tapestry.tldgen.javadoc.mapping.ParameterBean;
import fr.exanpe.tapestry.tldgen.javadoc.writer.JavadocTmpFileWriter;
import fr.exanpe.tapestry.tldgen.utils.MiscUtils;

/**
 * Generates an XML file that get Javadoc Information on a target class and all its
 * parameters.
 * Inspired from component-tapestry-report
 * To keep the -doclet parameter passed to javadoc simple, this class should not have any outside
 * dependencies.
 * 
 * @author lguerin
 */
public class ComponentsInfoBeanDoclet extends Doclet
{
    /**
     * Option to set the XML output result file
     */
    static String OUTPUT_PATH_OPTION = "-o";

    /**
     * Option to set the javadoc encoding
     */
    static String ENCODING_OPTION = "-encoding";

    /**
     * Output path for generate XML output file
     */
    static String outputPath;

    /**
     * Source file encoding
     */
    static String sourceEncoding;

    static class Worker
    {

        public void run(String outputPath, RootDoc root) throws Exception
        {

            File output = new File(outputPath);
            Writer writer = new FileWriter(output);

            ComponentsInfoBean infos = new ComponentsInfoBean();

            for (ClassDoc cd : root.classes())
            {
                buildComponentBean(cd, infos);
            }

            new JavadocTmpFileWriter(writer, sourceEncoding).write(infos);
        }

        /**
         * Collect Javadoc informations of the component
         * 
         * @param classDoc The class to analyze
         */
        private void buildComponentBean(ClassDoc classDoc, ComponentsInfoBean infos)
        {
            if (!classDoc.isPublic()) { return; }

            // Components must be root classes, not nested classes.
            if (classDoc.containingClass() != null) { return; }

            // Check for a no-args public constructor
            boolean found = false;
            for (ConstructorDoc cons : classDoc.constructors())
            {
                if (cons.isPublic() && cons.parameters().length == 0)
                {
                    found = true;
                    break;
                }
            }

            if (!found) { return; }

            ComponentBean bean = new ComponentBean();
            bean.setClassName(classDoc.qualifiedTypeName());
            bean.setSuperClassName(classDoc.superclass().qualifiedTypeName());
            bean.setDescription(MiscUtils.formatDescription(classDoc));

            for (FieldDoc fd : classDoc.fields())
            {
                if (fd.isStatic())
                {
                    continue;
                }
                if (!fd.isPrivate())
                {
                    continue;
                }

                Map<String, String> parameterAnnotationsValues = findAnnotation(fd, "Parameter");
                if (parameterAnnotationsValues != null)
                {
                    ParameterBean param = buildParameter(fd, parameterAnnotationsValues);
                    bean.addParameter(param);
                    continue;
                }
            }

            infos.addComponent(bean);

        }

        private ParameterBean buildParameter(FieldDoc fd, Map<String, String> parameterAnnotationValues)
        {
            String name = parameterAnnotationValues.get("name");
            if (name == null)
            {
                name = fd.name().replaceAll("^[$_]*", "");
            }

            ParameterBean param = new ParameterBean();
            param.setName(name);
            param.setDescription(MiscUtils.formatDescription(fd));

            return param;
        }

        private Map<String, String> findAnnotation(ProgramElementDoc doc, String name)
        {
            for (AnnotationDesc annotation : doc.annotations())
            {
                if (annotation.annotationType().qualifiedTypeName().equals("org.apache.tapestry5.annotations." + name))
                {
                    Map<String, String> result = new HashMap<String, String>();

                    for (ElementValuePair pair : annotation.elementValues())
                    {
                        result.put(pair.element().name(), pair.value().value().toString());
                    }
                    return result;
                }
            }
            return null;
        }

    }

    /**
     * Yes we are interested in annotations, etc.
     */
    public static LanguageVersion languageVersion()
    {
        return LanguageVersion.JAVA_1_5;
    }

    public static int optionLength(String option)
    {
        if (option.equals(OUTPUT_PATH_OPTION)) { return 2; }
        return 0;
    }

    public static boolean validOptions(String options[][], DocErrorReporter reporter)
    {
        for (String[] group : options)
        {
            if (group[0].equals(OUTPUT_PATH_OPTION))
            {
                outputPath = group[1];
            }

            if (group[0].equals(ENCODING_OPTION))
            {
                sourceEncoding = group[1];
            }
        }

        if (outputPath == null)
        {
            reporter.printError(String.format("Usage: javadoc %s path", OUTPUT_PATH_OPTION));
        }
        return true;
    }

    public static boolean start(RootDoc root)
    {
        // Enough of this static method bullshit. What the fuck were they thinking?
        try
        {
            new Worker().run(outputPath, root);
        }
        catch (Exception ex)
        {
            root.printError(ex.getMessage());
            return false;
        }
        return true;
    }
}
