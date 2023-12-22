package models.symbol.operator.binary;

import Operation.Calculation;

import java.util.ArrayList;

public class NewBinaryOperator extends Binary {
    private final String formula;
    private final Calculation calculation;

    public NewBinaryOperator(String symbols, String formula, Calculation calculation) {
        super(symbols, 1);
        this.calculation = calculation;
        this.formula = formula;
    }

    @Override
    public Number operate(Number a, Number b) throws Exception {
        String infix = this.replaceNumbers(a, b);
        ArrayList<String> postfix = calculation.convertInfixToPostfix(infix);
        String answer = calculation.calculatePostfix(postfix);
        return Double.parseDouble(answer);
    }

    // replace numbers :
    private String replaceNumbers(Number a, Number b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < formula.length(); i++) {
            if (formula.charAt(i) == 'a') {
                sb.append(a);
            } else if (formula.charAt(i) == 'b') {
                sb.append(b);
            } else
                sb.append(formula.charAt(i));
        }
        return sb.toString();
    }
}
