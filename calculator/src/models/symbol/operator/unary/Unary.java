package models.symbol.operator.unary;

import models.symbol.operator.Operator;
import models.symbol.operator.OperatorType;

public abstract class Unary extends Operator {
    public Unary(String symbols , int priority){
        super(symbols , OperatorType.UNARY , priority);
    }
    abstract public Number operate(Number a) throws Exception;
}
