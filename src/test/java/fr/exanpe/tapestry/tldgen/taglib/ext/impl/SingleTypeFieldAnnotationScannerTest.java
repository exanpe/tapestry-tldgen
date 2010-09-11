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

package fr.exanpe.tapestry.tldgen.taglib.ext.impl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import junit.framework.TestCase;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import fr.exanpe.tapestry.tldgen.ext.impl.SingleTypeFieldAnnotationScanner;
import fr.exanpe.test.heritage.components.Other;
import fr.exanpe.test.heritage.components.SubSubComponent;

public class SingleTypeFieldAnnotationScannerTest extends TestCase
{
    public void testOk() throws MalformedURLException
    {
        Reflections reflection = new Reflections(new ConfigurationBuilder().filterInputsBy(
                new FilterBuilder.Include(FilterBuilder.prefix(SubSubComponent.class.getPackage().getName()))).setUrls(new URL[]
        { new File("target/test-classes/").toURI().toURL() }).setScanners(new SingleTypeFieldAnnotationScanner(SubSubComponent.class)));

        Collection<String> fieldsAsString = reflection.getStore().get(SingleTypeFieldAnnotationScanner.class).values();

        assertEquals(3, fieldsAsString.size());
    }

    public void testOk2() throws MalformedURLException
    {
        Reflections reflection = new Reflections(new ConfigurationBuilder().filterInputsBy(
                new FilterBuilder.Include(FilterBuilder.prefix(Other.class.getPackage().getName()))).setUrls(new URL[]
        { new File("target/test-classes/").toURI().toURL() }).setScanners(new SingleTypeFieldAnnotationScanner(Other.class)));
        Collection<String> fieldsAsString = reflection.getStore().get(SingleTypeFieldAnnotationScanner.class).values();

        assertEquals(1, fieldsAsString.size());
    }
}
