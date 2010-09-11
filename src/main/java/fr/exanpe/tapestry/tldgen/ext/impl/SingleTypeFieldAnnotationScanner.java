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

import org.reflections.scanners.FieldAnnotationsScanner;

/**
 * Limit the search to a single class
 * 
 * @author attonnnn
 */
public class SingleTypeFieldAnnotationScanner extends FieldAnnotationsScanner
{
    private static final String FILE_CLASS_EXT = ".class";

    private Class<?> c;

    private boolean superClass;

    /**
     * Contructor
     * 
     * @param c the class to search
     * @param superClass if any superclass of the class passed in parameter must be accepted
     *            (default is true)
     */
    public SingleTypeFieldAnnotationScanner(Class<?> c, boolean superClass)
    {
        this.c = c;
        this.superClass = superClass;
    }

    /**
     * @see SingleTypeFieldAnnotationScanner#SingleTypeFieldAnnotationScanner(Class, boolean)
     * @param c the class to scan
     */
    public SingleTypeFieldAnnotationScanner(Class<?> c)
    {
        this(c, true);
    }

    @Override
    public boolean acceptsInput(String file)
    {
        // if not a java class
        if (!file.endsWith(FILE_CLASS_EXT))
            return false;

        // exact match
        if (file.equals(c.getName() + FILE_CLASS_EXT))
            return true;

        // no super class tested
        if (!superClass)
            return false;

        Class<?> testClass;
        try
        {
            testClass = formatFileToClass(file);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("Class loading error for class :" + file.substring(0, file.length() - FILE_CLASS_EXT.length()), e);
        }

        return testClass.isAssignableFrom(c);
    }

    /**
     * Return the class corresponding to the Java resource file returned
     * 
     * @param file the java resource file
     * @return the corresponding class loaded
     * @throws ClassNotFoundException if the class cannot be loaded
     */
    private Class<?> formatFileToClass(String file) throws ClassNotFoundException
    {
        return Class.forName(file.substring(0, file.length() - FILE_CLASS_EXT.length()));
    }
}
