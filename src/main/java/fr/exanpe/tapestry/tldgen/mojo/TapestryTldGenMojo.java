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

package fr.exanpe.tapestry.tldgen.mojo;

/*
 * Copyright 2001-2010 The Apache Software Foundation.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.classworlds.DuplicateRealmException;
import org.codehaus.plexus.util.ReaderFactory;

import fr.exanpe.tapestry.tldgen.javadoc.JavadocProcessor;
import fr.exanpe.tapestry.tldgen.javadoc.mapping.ComponentsInfoBean;
import fr.exanpe.tapestry.tldgen.taglib.builder.StructureBuilder;
import fr.exanpe.tapestry.tldgen.taglib.mapping.Taglib;
import fr.exanpe.tapestry.tldgen.taglib.writer.TaglibWriterProcessor;
import fr.exanpe.tapestry.tldgen.utils.JavadocBeanMerger;
import fr.exanpe.tapestry.tldgen.utils.MiscUtils;
import fr.exanpe.tapestry.tldgen.utils.TapestryTldGenConstants;

/**
 * A Tapestry tld generator tool for Tapestry components and Eclipse IDE.
 * Analyse the Tapestry components package by reflexion and generate a tld file.
 * The tld file result allow you to use code completion in tml files
 * via built in JSP Eclipse Editor.
 * 
 * @goal tld-generate
 * @requiresDependencyResolution compile
 * @configurator include-project-dependencies
 * @phase process-classes
 * @author attonnnn
 * @author lguerin
 */
public class TapestryTldGenMojo extends AbstractMojo
{
    /**
     * Location of the output file.
     * 
     * @parameter default-value="${project.build.directory}/${project.build.finalName}.tld"
     */
    private File outputFile;

    /**
     * Identifies the application root package.
     * 
     * @parameter
     * @required
     */
    private String rootPackage;

    /**
     * The URI used to identify your Tapestry library
     * 
     * @parameter
     * @required
     */
    private String uri;

    /**
     * The XML namespace used to call your own components
     * 
     * @parameter default-value="t"
     * @required
     */
    private String namespace;

    /**
     * The source file encoding
     * Default is the plateform encoding
     * 
     * @parameter
     */
    private String encoding;

    /**
     * The Maven Project Object
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * The original classpath to restore after execution
     */
    @SuppressWarnings("unused")
    private String originalClasspath;

    public void execute() throws MojoExecutionException
    {
        // init source file encoding
        if (encoding == null || "".equals(encoding.trim()))
        {
            getLog().warn(
                    "encoding parameter has not been set. Using plateform encoding for source file javadoc processing (" + ReaderFactory.FILE_ENCODING + ")");
            encoding = ReaderFactory.FILE_ENCODING;
        }

        // test source file encoding
        try
        {
            Charset.forName(encoding);
        }
        catch (UnsupportedCharsetException e)
        {
            getLog().error("The charset provided cannot be loaded by the JVM : " + encoding, e);
            return;
        }

        Taglib taglib = createTaglibModel(loadCustomClassLoader());

        // Collect informations provided by Javadoc
        JavadocProcessor javadocProcessor = new JavadocProcessor(project, this.getPluginLocalJarForDoclet(), getLog(), encoding);
        ComponentsInfoBean infos = javadocProcessor.runJavadoc(rootPackage);

        JavadocBeanMerger.mergeToTaglib(taglib, infos);

        try
        {
            // ENCODING TRANSFORMATION : HERE IS PERFORMED ENCODING_SRC > UTF-8
            TaglibWriterProcessor tldProcessor = new TaglibWriterProcessor(new OutputStreamWriter(new FileOutputStream(outputFile),
                    TapestryTldGenConstants.TLD_ENCODING));
            tldProcessor.write(taglib);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

    private Taglib createTaglibModel(URL[] urls) throws MojoExecutionException
    {
        StructureBuilder builder = new StructureBuilder(getLog());

        Taglib taglib = builder.build(rootPackage, TapestryTldGenConstants.TAPESTRY_SUPPORTED_SUBPACKAGES, urls);
        taglib.setTaglibVersion(MiscUtils.formatTlibVersion(project.getVersion()));
        taglib.setShortName(namespace);
        taglib.setUri(uri);

        return taglib;
    }

    /**
     * Initialize a custom ClassLoader in order to provide target/classes into classpath.
     * 
     * @return Custom classpath URLs array
     * @throws MojoExecutionException if errors occurs
     * @throws DuplicateRealmException
     */
    @SuppressWarnings("unchecked")
    private URL[] loadCustomClassLoader() throws MojoExecutionException
    {
        try
        {
            List classpathFiles = new ArrayList(project.getArtifacts());
            URL[] urls = new URL[classpathFiles.size() + 1];

            for (int i = 0; i < classpathFiles.size(); ++i)
            {
                urls[i] = ((DefaultArtifact) classpathFiles.get(i)).getFile().toURI().toURL();
            }

            urls[classpathFiles.size()] = new File(project.getBuild().getOutputDirectory()).toURI().toURL();

            return urls;
        }
        catch (MalformedURLException e)
        {
            throw new MojoExecutionException(e.getMessage(), e);
        }

    }

    /**
     * Needed to help locate this plugin's local JAR file for the -doclet argument.
     * 
     * @parameter default-value="${localRepository}"
     * @read-only
     */
    private ArtifactRepository localRepository;

    /**
     * Needed to help locate this plugin's local JAR file for the -doclet argument.
     * 
     * @parameter default-value="${plugin.groupId}"
     * @read-only
     */
    private String pluginGroupId;

    /**
     * Needed to help locate this plugin's local JAR file for the -doclet argument.
     * 
     * @parameter default-value="${plugin.artifactId}"
     * @read-only
     */
    private String pluginArtifactId;

    /**
     * Needed to help locate this plugin's local JAR file for the -doclet argument.
     * 
     * @parameter default-value="${plugin.version}"
     * @read-only
     */
    private String pluginVersion;

    /**
     * @return this plugin's local JAR file for the -doclet argument
     */
    private File getPluginLocalJarForDoclet()
    {
        File file = new File(localRepository.getBasedir());

        for (String term : pluginGroupId.split("\\."))
            file = new File(file, term);

        file = new File(file, pluginArtifactId);
        file = new File(file, pluginVersion);

        file = new File(file, String.format("%s-%s.jar", pluginArtifactId, pluginVersion));
        return file;
    }

}
