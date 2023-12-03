package models.symbol.operator.precedence;

public class OpeningBracket extends Precedence {
    public OpeningBracket() {
        super("[", 0 , PrecedenceType.BRACKET , Type.OPENING);
    }
}
