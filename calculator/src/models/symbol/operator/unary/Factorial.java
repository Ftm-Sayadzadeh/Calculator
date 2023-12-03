package models.symbol.operator.unary;

public class Factorial extends Unary {
    public Factorial() {
        super("!", 2);
    }

    @Override
    public Number operate(Number a) {
        int fact = 1;
        for( int i = 1 ; i <= a.intValue() ; i++)
            fact = fact * i;
        return fact;
    }
}
