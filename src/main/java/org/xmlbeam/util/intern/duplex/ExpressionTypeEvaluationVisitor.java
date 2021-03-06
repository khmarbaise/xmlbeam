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

import static org.xmlbeam.util.intern.duplex.ExpressionType.ATTRIBUTE;
import static org.xmlbeam.util.intern.duplex.ExpressionType.ELEMENT;
import static org.xmlbeam.util.intern.duplex.ExpressionType.NODE;
import static org.xmlbeam.util.intern.duplex.ExpressionType.VALUE;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTABBREVFORWARDSTEP;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTABBREVREVERSESTEP;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTADDITIVEEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTANDEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTANYFUNCTIONTEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTANYKINDTEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTATOMICTYPE;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTATTRIBNAMEORWILDCARD;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTATTRIBUTEDECLARATION;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTATTRIBUTENAME;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTATTRIBUTETEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTCASTABLEEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTCASTEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTCOMMENTTEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTCOMPARISONEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTCONTEXTITEMEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTDECIMALLITERAL;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTDOCUMENTTEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTDOUBLELITERAL;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTDYNAMICFUNCTIONINVOCATION;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTELEMENTDECLARATION;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTELEMENTNAME;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTELEMENTNAMEORWILDCARD;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTELEMENTTEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTENCLOSEDEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTFOREXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTFORWARDAXIS;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTFUNCTIONCALL;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTFUNCTIONITEMEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTFUNCTIONQNAME;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTFUNCTIONTEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTIFEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTINLINEFUNCTION;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTINSTANCEOFEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTINTEGERLITERAL;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTINTERSECTEXCEPTEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTITEMTYPE;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTLBRACE;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTLETEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTLITERALFUNCTIONITEM;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTMINUS;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTMULTIPLICATIVEEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTNAMESPACENODETEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTNAMETEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTNCNAME;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTNCNAMECOLONSTAR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTNODETEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTOCCURRENCEINDICATOR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTOREXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTPARAM;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTPARAMLIST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTPARENTHESIZEDEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTPARENTHESIZEDITEMTYPE;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTPATHEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTPITEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTPLUS;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTPREDICATE;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTPREDICATELIST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTQNAME;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTQUANTIFIEDEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTRANGEEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTRBRACE;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTREVERSEAXIS;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSCHEMAATTRIBUTETEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSCHEMAELEMENTTEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSEQUENCETYPE;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSIMPLEFORBINDING;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSIMPLELETBINDING;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSIMPLELETCLAUSE;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSINGLETYPE;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSLASH;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSLASHSLASH;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSTARCOLONNCNAME;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSTART;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSTEPEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTSTRINGLITERAL;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTTEXTTEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTTREATEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTTYPEDECLARATION;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTTYPEDFUNCTIONTEST;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTTYPENAME;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTUNARYEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTUNIONEXPR;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTVARNAME;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTVOID;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTWILDCARD;
import static org.xmlbeam.util.intern.duplex.XParserTreeConstants.JJTXPATH;

import java.util.EnumSet;

import org.w3c.dom.Node;

/**
 * @author sven
 */
class ExpressionTypeEvaluationVisitor implements INodeEvaluationVisitor<ExpressionType> {

    @Override
    public ExpressionType visit(final SimpleNode node, final Node data) {
        switch (node.getID()) {
        case JJTSTART:
        case JJTXPATH:
        case JJTEXPR:
        case JJTFUNCTIONCALL:
        case JJTSTEPEXPR:
            return node.firstChildAccept(this, data);
        case JJTPATHEXPR:
            return node.lastChildAccept(this, data);
        case JJTABBREVFORWARDSTEP:
            return "@".equals(node.getValue()) ? ATTRIBUTE : ELEMENT;
        case JJTFUNCTIONQNAME:
            return "root".equals(node.getValue()) ? ELEMENT : VALUE;
        case JJTUNIONEXPR:
            final EnumSet<ExpressionType> types = EnumSet.noneOf(ExpressionType.class);
            node.eachDirectChild(new VisitorClosure() {
                @Override
                public void apply(final SimpleNode node, final Node data) {
                    types.add(visit(node, data));
                }
            }, data);
            if (types.isEmpty()) {
                throw new IllegalStateException("Can not determine type of union expression. This may be a bug. Please report!");
            }
            if (types.size() == 1) {
                return types.iterator().next();
            }
            if (types.contains(VALUE)) {
                return VALUE;
            }
            return NODE;
        case JJTSTRINGLITERAL:
        case JJTINTEGERLITERAL:
        case JJTDECIMALLITERAL:
        case JJTDOUBLELITERAL:
        case JJTADDITIVEEXPR:
        case JJTMULTIPLICATIVEEXPR:
        case JJTCOMPARISONEXPR:
        case JJTOREXPR:
        case JJTANDEXPR:
            return VALUE;
        case JJTFORWARDAXIS:
            if ("self".equals(node.getValue())) {
                return NODE;
            }
            ;
            return "attribute".equals(node.getValue()) ? ATTRIBUTE : ELEMENT;
        case JJTREVERSEAXIS:
        case JJTABBREVREVERSESTEP:
            return ELEMENT;
        case JJTCONTEXTITEMEXPR:
            return NODE;
        case JJTSLASH:
            throw new XBXPathExprNotAllowedForWriting(node, "Expression does not select anything.");
        case JJTPARAMLIST:
        case JJTPARAM:
        case JJTENCLOSEDEXPR:
        case JJTLBRACE:
        case JJTRBRACE:
        case JJTVOID:
        case JJTFOREXPR:
        case JJTSIMPLEFORBINDING:
        case JJTLETEXPR:
        case JJTSIMPLELETCLAUSE:
        case JJTSIMPLELETBINDING:
        case JJTQUANTIFIEDEXPR:
        case JJTIFEXPR:
        case JJTRANGEEXPR:
        case JJTINTERSECTEXCEPTEXPR:
        case JJTINSTANCEOFEXPR:
        case JJTTREATEXPR:
        case JJTCASTABLEEXPR:
        case JJTCASTEXPR:
        case JJTUNARYEXPR:
        case JJTMINUS:
        case JJTPLUS:
        case JJTSLASHSLASH:
        case JJTNODETEST:
        case JJTNAMETEST:
        case JJTWILDCARD:
        case JJTNCNAMECOLONSTAR:
        case JJTSTARCOLONNCNAME:
        case JJTPREDICATELIST:
        case JJTPREDICATE:
        case JJTVARNAME:
        case JJTPARENTHESIZEDEXPR:
        case JJTFUNCTIONITEMEXPR:
        case JJTLITERALFUNCTIONITEM:
        case JJTINLINEFUNCTION:
        case JJTDYNAMICFUNCTIONINVOCATION:
        case JJTSINGLETYPE:
        case JJTTYPEDECLARATION:
        case JJTSEQUENCETYPE:
        case JJTOCCURRENCEINDICATOR:
        case JJTITEMTYPE:
        case JJTATOMICTYPE:
        case JJTANYKINDTEST:
        case JJTDOCUMENTTEST:
        case JJTTEXTTEST:
        case JJTCOMMENTTEST:
        case JJTNAMESPACENODETEST:
        case JJTPITEST:
        case JJTATTRIBUTETEST:
        case JJTATTRIBNAMEORWILDCARD:
        case JJTSCHEMAATTRIBUTETEST:
        case JJTATTRIBUTEDECLARATION:
        case JJTELEMENTTEST:
        case JJTELEMENTNAMEORWILDCARD:
        case JJTSCHEMAELEMENTTEST:
        case JJTELEMENTDECLARATION:
        case JJTATTRIBUTENAME:
        case JJTELEMENTNAME:
        case JJTTYPENAME:
        case JJTFUNCTIONTEST:
        case JJTANYFUNCTIONTEST:
        case JJTTYPEDFUNCTIONTEST:
        case JJTPARENTHESIZEDITEMTYPE:
        case JJTNCNAME:
        case JJTQNAME:
        default:
            break;
        }
        throw new IllegalStateException("Unknown Node " + node);
    }
}
