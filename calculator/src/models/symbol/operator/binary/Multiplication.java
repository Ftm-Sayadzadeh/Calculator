package models.symbol.operator.binary;

public class Multiplication extends Binary {
    public Multiplication() {
        super("*", 4);
    }

    @Override
    public Number operate(Number a, Number b) {
        return  a.doubleValue() *  b.doubleValue();
    }
}
