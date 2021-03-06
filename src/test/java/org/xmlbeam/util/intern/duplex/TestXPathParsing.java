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
package org.xmlbeam.util.intern.duplex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlbeam.XBProjector;
import org.xmlbeam.XBProjector.Flags;
import org.xmlbeam.annotation.XBDocURL;
import org.xmlbeam.annotation.XBRead;
import org.xmlbeam.config.DefaultXMLFactoriesConfig;
import org.xmlbeam.dom.DOMAccess;
import org.xmlbeam.util.intern.DOMHelper;

/**
 * @author sven
 */
@SuppressWarnings("javadoc")
@RunWith(Parameterized.class)
public class TestXPathParsing {

    @XBDocURL("resource://tests.xml")
    public interface Projection extends DOMAccess {
        @XBRead("/tests/test")
        List<Projection> getTests();

        @XBRead("./before/child::*")
        Projection getBefore();

        @XBRead("./after/child::*")
        Projection getAfter();

        @XBRead("./@id")
        String getTestId();

        @XBRead("./xpath")
        String getXPath();

        @XBRead("./contextNodePath")
        String getContextNodePath();

        @XBRead("./xpath/@value")
        String getXPathValue();

        @XBRead("./@skipSameValidation")
        boolean shouldSkipSameValidation();

    };

    private Document document;
    private final String testId;
    private final Projection before;
    private final String xpath;
    private final String value;
    private final Projection after;
    private final String contextPath;
    private final boolean skipSameValidation;

    private final static int RUN_ONLY = -1;

//    @Before
//    public void prepare() {
//        document = new DefaultXMLFactoriesConfig().createDocumentBuilder().newDocument();
//    }

    public TestXPathParsing(final String id, final Projection before, final String xpath, final String value, final String contextPath, final Projection after, final boolean skipSameValidation) {
        this.testId = id;
        this.before = before;
        this.xpath = xpath;
        this.value = value;
        this.after = after;
        this.contextPath = contextPath;
        this.skipSameValidation = skipSameValidation;
    }

    @Test
    public void testXPathParsing() throws Exception {
        //String xpath = "let $incr :=       function($n) {$n+1}  \n return $incr(2)";
//        if ((before!=null) && (before.getDOMNode()!=null)) {
//            Node node = before.getDOMNode().cloneNode(true);
//            document.adoptNode(node);
//            document.appendChild(node);
//        }
        document = before.getDOMOwnerDocument();
        // String xpath = "/hoo[@id='wutz']/foo/loo";
        createByXParser(xpath, value);
        XBProjector projector = new XBProjector(Flags.TO_STRING_RENDERS_XML, Flags.ABSENT_IS_EMPTY);
        Projection result = projector.projectDOMNode(document, Projection.class);
        after.getDOMNode().normalize();
        result.getDOMNode().normalize();
        DOMHelper.trim(after.getDOMNode());
        DOMHelper.trim(result.getDOMNode());
        after.getDOMOwnerDocument().normalizeDocument();
        result.getDOMOwnerDocument().normalizeDocument();
        DOMHelper.nodesAreEqual(after.getDOMNode(), result.getDOMNode());
        assertEquals(after.toString(), result.toString());
    }

    @Parameters
    public static Collection<Object[]> tests() throws Exception {
        List<Object[]> params = new LinkedList<Object[]>();
        Projection testDefinition = new XBProjector(Flags.TO_STRING_RENDERS_XML, Flags.ABSENT_IS_EMPTY).io().fromURLAnnotation(Projection.class);
        int count = 0;
        for (Projection test : testDefinition.getTests()) {
            final Object[] param = new Object[] { //
            "[" + count + "] " + test.getTestId(),//
                    subProjectionToDocument(test.getBefore()),//
                    test.getXPath().trim(),//
                    test.getXPathValue().trim(),//
                    test.getContextNodePath().trim(),//
                    subProjectionToDocument(test.getAfter()),//
                    test.shouldSkipSameValidation(),//
            };
            if ((count == RUN_ONLY) || (RUN_ONLY < 0)) {
                params.add(param);
            }
            ++count;
        }
        return params;
    }

    /**
     * @param test
     * @return
     */
    private static Projection subProjectionToDocument(final Projection test) {
        new DefaultXMLFactoriesConfig().createDocumentBuilder().isNamespaceAware();
        Document document = new DefaultXMLFactoriesConfig().createDocumentBuilder().newDocument();

        if (test != null) {
            Node node = test.getDOMNode().cloneNode(true);
            document.adoptNode(node);
            document.appendChild(node);
        }

        return new XBProjector(Flags.TO_STRING_RENDERS_XML).projectDOMNode(document, Projection.class);
    }

    /**
     * @param xpath
     * @throws ParseException
     * @throws Exception
     */
    private void createByXParser(final String xpath, final String value) throws ParseException, Exception {
        {// Just debug helping output
            XParser parser = new XParser(new StringReader(xpath));
            SimpleNode node = parser.START();
            System.out.println("-----------------------------------------");
            System.out.println(testId);
            System.out.println(xpath);
            node.dump("");
        }

        final Node contextNode = contextPath.isEmpty() ? document : evalViaXPath(contextPath, document);

        final DuplexExpression duplex = new DuplexXPathParser(Collections.<String, String> emptyMap()).compile(xpath);
        final Node newNode = duplex.ensureExistence(contextNode);

        assertNotNull(newNode);

        if (!value.isEmpty()) {
            DOMHelper.setDirectTextContent(newNode, value);
        }

        if (!skipSameValidation) {
            // Evaluate expression a second time
            final Node paranoiaNode = evalViaXPath(xpath, contextNode);
            evalViaXPath("/project/repositories/repository[id='spring-libs-snapshot']/id", contextNode);

            // second result must select our just created node
            //   System.out.println(System.identityHashCode(paranoiaNode)+" "+System.identityHashCode(newNode));
            assertSame(paranoiaNode, newNode);
        }
    }

    private Node evalViaXPath(final String xpath, final Node contextNode) throws XPathExpressionException {
        if (xpath.endsWith("/@xmlns")) {
            String pathToElement = xpath.substring(0, xpath.length() - "/@xmlns".length());
            Element e = (Element) evalViaXPath(pathToElement, contextNode);
            return e.getAttributeNode("xmlns");
        }
        XPath xPath2 = new DefaultXMLFactoriesConfig().createXPath(document);
        XPathExpression expression = xPath2.compile(xpath);
        Object object = expression.evaluate(contextNode, XPathConstants.NODE);
        return (Node) object;
    }

    public void print() {
        final StringWriter writer = new StringWriter();
        try {
            new DefaultXMLFactoriesConfig().createTransformer().transform(new DOMSource(document), new StreamResult(writer));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        final String output = writer.getBuffer().toString();
        System.out.println(output);
    }
}
