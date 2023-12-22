package models.symbol.operator.binary;

public class Minus extends Binary {
    public Minus() {
        super("-", 5);
    }

    @Override
    public Number operate(Number a , Number b) {
        return a.doubleValue() - b.doubleValue();
    }
}
