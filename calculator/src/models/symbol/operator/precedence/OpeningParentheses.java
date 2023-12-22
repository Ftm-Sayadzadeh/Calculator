package models.symbol.operator.precedence;

public class OpeningParentheses extends Precedence{
    public OpeningParentheses() {
        super("(",0 , PrecedenceType.PARENTHESES , Type.OPENING);
    }
}
