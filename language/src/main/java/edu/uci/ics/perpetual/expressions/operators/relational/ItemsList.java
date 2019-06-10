
package edu.uci.ics.perpetual.expressions.operators.relational;

/**
 * Values of an "INSERT" statement (for example a SELECT or a list of expressions)
 */
public interface ItemsList {

    void accept(ItemsListVisitor itemsListVisitor);
}
