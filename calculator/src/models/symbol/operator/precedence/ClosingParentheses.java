package models.symbol.operator.precedence;

public class ClosingParentheses extends Precedence {
    public ClosingParentheses() {
        super(")", 0 , PrecedenceType.PARENTHESES , Type.CLOSING);
    }
}
