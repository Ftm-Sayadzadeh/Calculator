package models.symbol.operator.binary;

import models.symbol.operator.Operator;
import models.symbol.operator.OperatorType;

public abstract class Binary extends Operator {
    public Binary(String symbols , int priority){
        super(symbols , OperatorType.BINARY , priority);
    }
    public abstract Number operate(Number a , Number b) throws Exception;
}
