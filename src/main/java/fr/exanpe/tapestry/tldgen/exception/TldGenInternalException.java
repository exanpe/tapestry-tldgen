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

package fr.exanpe.tapestry.tldgen.exception;

/**
 * Base exception for Tapesty-tldgen.
 * 
 * @author lguerin
 */
public class TldGenInternalException extends Exception
{
    private static final long serialVersionUID = 2865892691251750372L;

    public TldGenInternalException()
    {
        super();
    }

    public TldGenInternalException(String msg, Throwable t)
    {
        super(msg, t);
    }

    public TldGenInternalException(String msg)
    {
        super(msg);
    }

    public TldGenInternalException(Throwable t)
    {
        super(t);
    }
}
