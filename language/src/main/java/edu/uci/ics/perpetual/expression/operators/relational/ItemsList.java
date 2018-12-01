
package edu.uci.ics.perpetual.expression.operators.relational;

/**
 * Values of an "INSERT" statement (for example a SELECT or a list of expressions)
 */
public interface ItemsList {

    void accept(ItemsListVisitor itemsListVisitor);
}
