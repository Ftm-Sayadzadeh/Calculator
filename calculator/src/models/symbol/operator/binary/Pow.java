package models.symbol.operator.binary;


public class Pow extends Binary {
    public Pow() {
        super("^", 3);
    }

    @Override
    public Number operate(Number a, Number b) {
        return  Math.pow(a.doubleValue() , b.doubleValue());
    }
}
