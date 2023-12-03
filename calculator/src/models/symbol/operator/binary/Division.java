package models.symbol.operator.binary;

public class Division extends Binary {
    public Division() {
        super("/", 4);
    }

    @Override
    public Number operate(Number a , Number b) throws Exception {
        if(b.doubleValue() == 0)
            throw new Exception("error : divide by zero");
        return  a.doubleValue() /  b.doubleValue();
    }
}
