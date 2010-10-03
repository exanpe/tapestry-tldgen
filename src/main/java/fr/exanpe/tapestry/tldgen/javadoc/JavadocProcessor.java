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

/**
 * 
 */
package fr.exanpe.tapestry.tldgen.javadoc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.DefaultConsumer;

import fr.exanpe.tapestry.tldgen.doclet.ComponentsInfoBeanDoclet;
import fr.exanpe.tapestry.tldgen.javadoc.mapping.ComponentsInfoBean;
import fr.exanpe.tapestry.tldgen.javadoc.reader.ComponentsInfoBuilder;
import fr.exanpe.tapestry.tldgen.javadoc.reader.impl.XStreamComponentsInfoBuilder;
import fr.exanpe.tapestry.tldgen.utils.TapestryTldGenConstants;

/**
 * Execute Javadoc tool
 * 
 * @author lGuerin
 */
public class JavadocProcessor
{
    /**
     * The Maven Project Object
     */
    private MavenProject project;

    /**
     * Logger
     */
    public final Log log;

    /**
     * Current plugin file into repository
     */
    private File currentPluginFile;

    /**
     * Source file encoding
     */
    private String sourceEncoding;

    /**
     * @param project
     * @param log
     */
    public JavadocProcessor(MavenProject project, File currentPluginFile, Log log, String sourceEncoding)
    {
        super();
        this.project = project;
        this.currentPluginFile = currentPluginFile;
        this.log = log;
        this.sourceEncoding = sourceEncoding;
    }

    /**
     * Run Javadoc tool in order to collect informations on components and parameters.
     * The result is compute into a custom bean.
     * 
     * @throws MojoExecutionException
     * @return A bean that contains informations provided by Javadoc
     */
    @SuppressWarnings("unchecked")
    public ComponentsInfoBean runJavadoc(String rootPackage) throws MojoExecutionException
    {
        log.info("Running JavaDoc to collect components parameter data ...");
        ComponentsInfoBean infos = null;

        Commandline command = new Commandline();

        try
        {
            command.setExecutable(pathToJavadoc());
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Unable to locate javadoc command: " + e.getMessage(), e);
        }

        // Sources path
        List<String> roots = project.getCompileSourceRoots();

        // Subpackages to recursively load
        String subpackages = "";
        for (final String tapestryPkgName : TapestryTldGenConstants.TAPESTRY_SUPPORTED_SUBPACKAGES)
        {
            String pkgname = rootPackage + "." + tapestryPkgName;
            subpackages += pkgname + ":";
        }

        // XML Output file for collect conponents information
        String tempDir = System.getProperty("java.io.tmpdir");
        // Security check if there is a trailing slash or not
        if (!(tempDir.endsWith("/") || tempDir.endsWith("\\")))
        {
            tempDir = tempDir + File.separator;
        }
        String parametersPath = tempDir + "tldgen-components-info.xml";

        // Args used by Javadoc tool
        String[] arguments =
        { "-private", "-o", parametersPath, "-subpackages", subpackages, "-doclet", ComponentsInfoBeanDoclet.class.getName(), "-docletpath", docletPath(),
                "-sourcepath", toArgumentPath(roots), "-encoding", sourceEncoding, "-classpath", classPath() };
        command.addArguments(arguments);

        // Execute Javadoc
        executeCommand(command);

        // Collect infos provided by Javadoc
        infos = collectJavadocOutputInfos(parametersPath);
        return infos;
    }

    private static final char QUOTE = '"';

    private ComponentsInfoBean collectJavadocOutputInfos(String path) throws MojoExecutionException
    {
        ComponentsInfoBean infos = null;
        Reader reader = null;
        try
        {
            reader = new FileReader(path);
        }
        catch (FileNotFoundException e)
        {
            throw new MojoExecutionException("Unable to collect infos provided by Javadoc: " + e.getMessage(), e);
        }

        ComponentsInfoBuilder builder = new XStreamComponentsInfoBuilder();

        infos = builder.readXMLOutput(reader);

        return infos;
    }

    private String toArgumentPath(List<String> paths)
    {
        StringBuilder builder = new StringBuilder(5000).append(QUOTE);

        String sep = "";

        for (String path : paths)
        {
            builder.append(sep);
            builder.append(path);

            sep = SystemUtils.PATH_SEPARATOR;
        }

        return builder.append(QUOTE).toString();
    }

    /**
     * Launch an external command
     * 
     * @param command The Commandline to execute
     * @throws MojoExecutionException
     */
    private void executeCommand(Commandline command) throws MojoExecutionException
    {
        log.debug(command.toString());
        CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();

        try
        {
            int exitCode = CommandLineUtils.executeCommandLine(command, new DefaultConsumer(), err);

            if (exitCode != 0)
            {
                String message = String.format("Javadoc exit code: %d - %s\nCommand line was: %s", exitCode, err.getOutput(), command);
                throw new MojoExecutionException(message);
            }
        }
        catch (CommandLineException ex)
        {
            throw new MojoExecutionException("Unable to execute javadoc command: " + ex.getMessage(), ex);
        }

        // ----------------------------------------------------------------------
        // Handle Javadoc warnings
        // ----------------------------------------------------------------------

        if (StringUtils.isNotEmpty(err.getOutput()))
        {
            log.info("Javadoc Warnings");

            StringTokenizer token = new StringTokenizer(err.getOutput(), "\n");
            while (token.hasMoreTokens())
            {
                String current = token.nextToken().trim();
                log.warn(current);
            }
        }
    }

    private String pathToJavadoc() throws IOException, MojoExecutionException
    {
        String executableName = SystemUtils.IS_OS_WINDOWS ? "javadoc.exe" : "javadoc";

        File executable = initialGuessAtJavadocFile(executableName);

        if (!executable.exists() || !executable.isFile()) { throw new MojoExecutionException(String.format(
                "Path %s does not exist or is not a file.",
                executable)); }
        return executable.getAbsolutePath();
    }

    private File initialGuessAtJavadocFile(String executableName)
    {
        if (SystemUtils.IS_OS_MAC_OSX)
            return new File(SystemUtils.getJavaHome() + File.separator + "bin", executableName);

        return new File(SystemUtils.getJavaHome() + File.separator + ".." + File.separator + "bin", executableName);
    }

    @SuppressWarnings("unchecked")
    private String classPath() throws MojoExecutionException
    {
        List<Artifact> artifacts = project.getCompileArtifacts();

        return artifactsToArgumentPath(artifacts);
    }

    private String artifactsToArgumentPath(List<Artifact> artifacts) throws MojoExecutionException
    {
        List<String> paths = new ArrayList<String>();

        for (Artifact artifact : artifacts)
        {
            if (artifact.getScope().equals("test"))
            {
                continue;
            }
            File file = artifact.getFile();

            if (file == null) { throw new MojoExecutionException("Unable to execute Javadoc: compile dependencies are not fully resolved."); }
            paths.add(file.getAbsolutePath());
        }
        paths.add(project.getBuild().getOutputDirectory() + File.separator);
        return toArgumentPath(paths);
    }

    /**
     * Specify the path to find Doclet class file
     * 
     * @return doclet path
     * @throws MojoExecutionException
     */
    private String docletPath() throws MojoExecutionException
    {
        return toArgumentPath(Arrays.asList(currentPluginFile.getAbsolutePath()));
    }
}
