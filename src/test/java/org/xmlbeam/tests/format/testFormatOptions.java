/**
 *  Copyright 2014 Sven Ewald
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
package org.xmlbeam.tests.format;

import org.junit.Test;
import org.xmlbeam.XBProjector;
import org.xmlbeam.util.intern.duplex.DuplexExpression;
import org.xmlbeam.util.intern.duplex.DuplexXPathParser;

/**
 * @author sven
 */
public class testFormatOptions {

    @Test
    public void testFormatOptions() {
/*
        ProjectionWithFormats projection = new XBProjector().projectEmptyDocument(ProjectionWithFormats.class);
        projection.getDate3();
        */
        DuplexExpression expression = new DuplexXPathParser().compile("/foo/bar/date using YYYYMMDD");
        expression.dump();
        System.out.println(expression.getExpressionFormatPattern());
    }

}