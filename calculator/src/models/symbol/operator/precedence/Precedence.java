package models.symbol.operator.precedence;

import models.symbol.operator.Operator;
import models.symbol.operator.OperatorType;

public abstract class Precedence extends Operator {
    private PrecedenceType precedenceType;
    private Type type;
    public Precedence(String symbols, int priority , PrecedenceType precedenceType , Type type) {
        super(symbols, OperatorType.PRECEDENCE , priority);
        this.precedenceType = precedenceType;
        this.type = type;
    }

    public PrecedenceType getPrecedenceType() {
        return precedenceType;
    }
    public Type getConditionType() {
        return type;
    }
}
