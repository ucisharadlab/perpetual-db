/*
 * #%L
 * JSQLParser library
 * %%
 * Copyright (C) 2004 - 2017 JSQLParser
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */
package edu.uci.ics.perpetual.util.cnfexpression;

import java.util.List;

import edu.uci.ics.perpetual.expressions.Expression;
import edu.uci.ics.perpetual.expressions.ExpressionVisitor;
import edu.uci.ics.perpetual.expressions.NullValue;
import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

/**
 * This is a helper class that mainly used for handling the CNF conversion.
 * @author messfish
 *
 */
public abstract class MultipleExpression extends ASTNodeAccessImpl implements Expression {

    private final List<Expression> childlist;
    
    public MultipleExpression(List<Expression> childlist) {
        this.childlist = childlist;
    }
    
    public int size() {
        return childlist.size();
    }
    
    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(new NullValue());
    }
    
    public List<Expression> getList() {
        return childlist;
    }
    
    public Expression getChild(int index) {
        return childlist.get(index);
    }
    
    public Expression removeChild(int index) {
        return childlist.remove(index);
    }
    
    public void setChild(int index, Expression express) {
        childlist.set(index, express);
    }
    
    public int getIndex(Expression express) {
        return childlist.indexOf(express);
    }
    
    public void addChild(int index, Expression express) {
        childlist.add(index, express);
    }
    
    public abstract String getStringExpression();
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i=0; i<size(); i++) {
            sb.append(getChild(i));
            if(i!=size() - 1) {
                sb.append(" ").append(getStringExpression()).append(" ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
    
}
