package edu.uci.ics.perpetual.predicate;

public enum ComparisionOperator {

    EQ("="),
    NEQ("!="),
    LT("<"),
    GT(">");

    private String str;
    ComparisionOperator(String str) {
        this.str = str;
    }

    @Override
    public String toString(){
        return str;
    }
}
