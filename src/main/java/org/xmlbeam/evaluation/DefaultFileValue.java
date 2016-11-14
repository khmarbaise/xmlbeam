/**
 *  Copyright 2016 Sven Ewald
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.xmlbeam.evaluation;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

import org.w3c.dom.Node;
import org.xmlbeam.XBProjected;
import org.xmlbeam.refcards.XBAutoListRefCard;
import org.xmlbeam.types.CloseableValue;
import org.xmlbeam.types.XBAutoValue;

/**
 * @author sven
 *
 */
public class DefaultFileValue<E> extends XBProjected<E> implements CloseableValue<E> {

    
    /**
     * @param baseNode
     * @param invocationContext
     */
    public DefaultFileValue(Node baseNode, InvocationContext invocationContext,Closeable documentWriter) {
        super(baseNode, invocationContext);
        this.documentWriter=documentWriter;
    }


    private final Closeable documentWriter;
    
 
    /**
     * @throws IOException
     * @see java.io.Closeable#close()
     */
    @Override
    public void close() throws IOException {
       documentWriter.close();
    }


   
}
