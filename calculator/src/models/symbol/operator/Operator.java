package models.symbol.operator;

import models.symbol.Symbol;

public class Operator extends Symbol {
    private final OperatorType type;
    private final int priority;
    public Operator( String symbols , OperatorType type , int priority){
        super(symbols);
        this.type = type ;
        this.priority = priority;
    }
    public OperatorType getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }
}
