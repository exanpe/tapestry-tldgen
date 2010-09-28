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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.tapestry5.annotations.Parameter;
import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.scanners.TypesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.reflections.util.Utils;

import com.google.common.collect.Multimap;

import fr.exanpe.tapestry.tldgen.ext.impl.SingleTypeFieldAnnotationScanner;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Attribute;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Tag;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Taglib;
import fr.exanpe.tapestry.tldgen.utils.TapestryTldGenConstants;

/**
 * This class builds the final structure of the taglib to generate
 * 
 * @author attonnnn
 */
public class StructureBuilder
{
    private final Log log;

    /**
     * Contructor
     * 
     * @param log the plugin log to output
     */
    public StructureBuilder(Log log)
    {
        this.log = log;
    }

    /**
     * Builds the output taglib structure
     * 
     * @param rootPackage the root package to look the components for
     * @param supportedPackages all sub packages to scan
     * @param urls the urls used to scan the packages
     * @return the structure containing the information on the taglib to generate
     * @throws MojoExecutionException if any unexpected error occurs
     */
    public Taglib build(String rootPackage, String[] supportedPackages, URL[] urls) throws MojoExecutionException
    {
        Taglib taglib = new Taglib();

        log.debug("Creating taglib object model...");


        for(String subPackage : supportedPackages){
            String pkgname = rootPackage +"."+ subPackage;

            log.debug("Processing taglib for full package named : " + pkgname);

            Reflections reflections = new Reflections(new ConfigurationBuilder().filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix(pkgname)))
                    .setUrls(urls).setScanners(new TypesScanner()));

            Store store = reflections.getStore();

            // Return classes anaylised by TypeScanner
            Multimap<String, String> classes = store.getStoreMap().values().iterator().next();

            log.debug(String.format("%s classes to analyse for %s package...", classes.keySet().size(), pkgname));

            // Loop on found classes
            for (final String s : classes.keySet())
            {
                Class<?> c;
                try
                {
                    log.debug(String.format("Load class %s into classloader", s));
                    c = Thread.currentThread().getContextClassLoader().loadClass(s);
                }
                catch (ClassNotFoundException e)
                {
                    // should not happen as it has just been parsed by Reflection...
                    log.error(e);
                    throw new MojoExecutionException("Class loader internal error for class :" + s, e);
                }

                if (!c.isAnnotation() && !c.isAnonymousClass() && !c.isEnum() && !c.isInterface() && !c.isLocalClass() && !c.isMemberClass()
                        && !c.isSynthetic() && !Modifier.isAbstract(c.getModifiers()))
                {
                    log.debug("Processing Tag : " + c.getName());

                    Tag tag = buildTagFromClass(rootPackage, c);
                    taglib.getTags().add(tag);
                }
            }
        }


        log.debug("Taglib object model completed");
        return taglib;
    }

    /**
     * Return the field annotated with @Parameter for a class
     * 
     * @param rootPackage The rootPackage to look for class and eventually parent abstract class
     * @param c the class
     * @return a list of {@link Field}
     */
    private List<Field> getFieldAnnotatedWithParameter(String rootPackage, Class<?> c)
    {
        Reflections reflection = new Reflections(rootPackage, new SingleTypeFieldAnnotationScanner(c).filterResultsBy(new FilterBuilder.Include(
                Parameter.class.getCanonicalName())));
        Collection<String> fieldsAsString = reflection.getStore().get(SingleTypeFieldAnnotationScanner.class).values();
        List<Field> fields = new ArrayList<Field>();
        for (String fAsString : fieldsAsString)
        {
            fields.add(Utils.getFieldFromString(fAsString));
        }
        return fields;
    }

    /**
     * Build the &lt;tag&gt; tag corresponding to a class
     * 
     * @param rootPackage The rootPackage to look for class and eventually parent abstract class
     * @param c the class for the tag
     * @return the {@link Tag} build
     */
    private Tag buildTagFromClass(String rootPackage, Class<?> c)
    {
        Tag tag = new Tag();
        tag.setTagClass(c.getName());
        tag.setName(c.getSimpleName());

        List<Field> fields = getFieldAnnotatedWithParameter(rootPackage, c);

        for (Field f : fields)
        {
            tag.getAttributes().add(buildAttributeFromField(f));
        }

        return tag;
    }

    /**
     * Build the &lt;attribute&gt; tag corresponding to a field
     * 
     * @param field the field
     * @return the {@link Attribute} object
     */
    private Attribute buildAttributeFromField(Field field)
    {
        log.debug("Processing Attribute : " + field.getName());

        Attribute attribute = new Attribute();
        attribute.setName(StringUtils.isNotEmpty(field.getAnnotation(Parameter.class).name())?field.getAnnotation(Parameter.class).name():field.getName());

        attribute.setDeferredValue(field.getType().getName());
        attribute.setRequired(field.getAnnotation(Parameter.class).required());
        attribute.setParameterDescription(buildParameterDescription(field.getAnnotation(Parameter.class)));
        
        return attribute;
    }
    
    private String buildParameterDescription(Parameter p){
        StringBuilder builder = new StringBuilder();
        
        builder.append(TapestryTldGenConstants.ALLOW_NULL_TXT).append(p.allowNull()).append("\n");
        builder.append(TapestryTldGenConstants.DEFAULT_TLD_SEPARATOR).append(TapestryTldGenConstants.DEFAULT_PREFIX_TXT).append(p.defaultPrefix()).append("\n");
        
        return builder.toString();
    }
}
