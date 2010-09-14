
## Available goals

 * tapestry-tldgen:tld-generate


## Setting up tapestry-tldgen

This requires only 2 minutes.

 1. Carefully javadoc your component classes and their attributes annotated with @Parameter (already done i hope, isn't it ? :))
 2. Open the pom.xml in your library project
 3. Add the following configuration in your plugins descriptor :

      <plugin>
        <groupId>fr.exanpe</groupId>
        <artifactId>tapestry-tldgen</artifactId>
        <version>1.0.0</version>
        <executions>
            <execution>
                <goals>
                    <goal>tld-generate</goal>
                </goals>
                <phase>process-classes</phase>
                <configuration>
                    <uri>${set your tld uri here}</uri>
                    <namespace>${set your tld defaut namespace here}</namespace>
                    <rootPackage>${set your root package here}</rootPackage>
                    <encoding>${set your source file encoding here}</encoding>
                </configuration>
            </execution>
        </executions>
      </plugin>    

 4. Run a build and get your tld from target/ folder
 
## Plugin Configuration

<table>
            <tr>
                <td>
                       <b>Configuration key     </b>
                </td>
                <td>
                       <b>Required   </b>
                </td>
                <td>
                       <b>Description</b>
                </td>
           </tr>
          <tr>
                <td>uri</td>
                <td><b>true</b></td>
                <td>The uri of your taglib. </td>
            </tr>
           <tr>
                <td>rootPackage</td>
                <td><b>true</b></td>
                <td>The root package to scan the components. Do not append "components" in the end, root only is required. </td>
            </tr>
            <tr>
                <td>namespace</td>
                <td>false</td>
                <td> The namespace of the taglib. Default is "t" . </td>
            </tr>
            <tr>
                <td>encoding</td>
                <td>false</td>
                <td>The source file encoding. Default is your plateform encoding. </td>
            </tr>
            <tr>
                <td>outputFile</td>
                <td>false</td>
                <td>The tld file location. Default is ${project.build.directory}/${project.build.finalName}.tld </td>
            </tr>
</table>

