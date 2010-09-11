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

import java.util.regex.Pattern;

import com.sun.javadoc.Doc;
import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Tag;

/**
 * Misc utils class
 * 
 * @author attonnnn
 */
public class MiscUtils
{
    /**
     * Dash char
     */
    private static final String DASH = "-";

    /**
     * Stripper for javadoc
     */
    private static final Pattern stripper = Pattern.compile("(<.*?>|&.*?;)", Pattern.DOTALL);

    /**
     * Format the version to be compliant with the tld requirements (dewey decimal)
     * 
     * @param version
     * @return the version formatted
     */
    public static String formatTlibVersion(String version)
    {
        if (version.contains(DASH)) { return version.substring(0, version.indexOf(DASH)); }
        return version;
    }

    /**
     * Format the description read from Javadoc
     * 
     * @param holder the Javadoc object
     * @return a correct String representing the description to output
     */
    public static String formatDescription(Doc holder)
    {
        StringBuilder builder = new StringBuilder();

        for (Tag tag : holder.inlineTags())
        {
            if (tag.name().equals("Text"))
            {
                builder.append(tag.text());
                continue;
            }

            if (tag.name().equals("@link"))
            {
                SeeTag seeTag = (SeeTag) tag;

                String label = seeTag.label();
                if (label != null && !label.equals(""))
                {
                    builder.append(label);
                    continue;
                }

                if (seeTag.referencedClassName() != null)
                {
                    builder.append(seeTag.referencedClassName());
                }
                if (seeTag.referencedMemberName() != null)
                {
                    builder.append("#");
                    builder.append(seeTag.referencedMemberName());
                }
            }
        }

        String text = builder.toString();

        // Fix it up a little.
        // Remove any simple open or close tags found in the text, as well as any XML entities.
        String stripped = stripper.matcher(text).replaceAll("");

        return stripped;
    }
}
