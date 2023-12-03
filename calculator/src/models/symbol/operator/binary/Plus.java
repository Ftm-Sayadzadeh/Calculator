package models.symbol.operator.binary;

public class Plus extends Binary {
    public Plus() {
        super("+", 5);
    }

    @Override
    public Number operate(Number a, Number b) {
        return  a.doubleValue() +  b.doubleValue();
    }
}
