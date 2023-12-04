package models.symbol.operator.unary;

import Operation.Calculation;

import java.util.ArrayList;

public class NewUnaryOperator extends Unary {
    public final String formula;
    private final Calculation calculation;

    public NewUnaryOperator(String symbols, String formula, Calculation calculation) {
        super(symbols, 1);
        this.formula = formula;
        this.calculation = calculation;
    }

    @Override
    public Number operate(Number a) throws Exception {
        String infix = this.replaceNumbers(a);
        ArrayList<String> postfix = calculation.convertInfixToPostfix(infix);
        String answer = calculation.calculatePostfix(postfix);
        return Double.parseDouble(answer);
    }

    // replace numbers :
    private String replaceNumbers(Number a) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == 'a') {
                sb.append(a);
            } else
                sb.append(formula.charAt(i));
        }
        return sb.toString();
    }
}
