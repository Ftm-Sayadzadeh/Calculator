package models.symbol.operator.precedence;

public class ClosingBracket extends Precedence{
    public ClosingBracket() {
        super("]", 0 , PrecedenceType.BRACKET , Type.CLOSING);
    }
}
