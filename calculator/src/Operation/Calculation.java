package Operation;

import models.dataStructures.LinkedStack;
import models.symbol.Symbol;
import models.symbol.constant.Constant;
import models.symbol.constant.E;
import models.symbol.constant.PI;
import models.symbol.operator.Operator;
import models.symbol.operator.binary.*;
import models.symbol.operator.precedence.*;
import models.symbol.operator.unary.Factorial;
import models.symbol.operator.unary.Unary;

import java.util.ArrayList;
import java.util.Objects;

public class Calculation {
    public static ArrayList<Symbol> validSymbols = new ArrayList<>();

    public Calculation() {
        validSymbols.add(new Plus());
        validSymbols.add(new Minus());
        validSymbols.add(new Multiplication());
        validSymbols.add(new Division());
        validSymbols.add(new Factorial());
        validSymbols.add(new Pow());
        validSymbols.add(new PI());
        validSymbols.add(new E());
        validSymbols.add(new OpeningBracket());
        validSymbols.add(new OpeningParentheses());
        validSymbols.add(new ClosingBracket());
        validSymbols.add(new ClosingParentheses());
    }

    // getter ----------------------------------------------------

    public ArrayList<Symbol> getValidSymbols() {
        return validSymbols;
    }

    // METHOD : ----------------------------------------------------
    public String calculate(String expression) throws Exception {
        // check validation
        Validation validation = new Validation(validSymbols);
        validation.checkValidation(expression);

        // calculation
        ArrayList<String> postfix = this.convertInfixToPostfix(expression);
        String answer = this.calculatePostfix(postfix);

        // handel type of answer ( double or integer ) :
        if (Objects.requireNonNull(answer).contains(".")) {
            // we need to know type of answer cuz in integer case we need to remove ".00"
            String[] dividedStr = Objects.requireNonNull(answer).split("\\.");
            if (Objects.equals(dividedStr[1], "0"))
                return dividedStr[0];
            else
                return answer;
        } else
            return answer;
    }

    // convert infix to postfix before calculating expression ------------------------
    public ArrayList<String> convertInfixToPostfix(String expression) {
        ArrayList<String> result = new ArrayList<>();
        LinkedStack<Operator> stack = new LinkedStack<>();
        int i = 0;

        Symbol previousSymbol = null;
        while (i <= expression.length() - 1) {
            Validation validation = new Validation(validSymbols);
            Symbol symbol = validation.isSymbol(expression, i);
            if (symbol != null) {
                // when symbol is constant we need to add it into result
                if (symbol instanceof Constant) {
                    i = i + symbol.getSymbols().length();
                    result.add(symbol.getSymbols());
                }
                /* when symbol is precedence we need to check  ->
                if it is opening :
                 - we just need to push it into stack
                if it is closing :
                 - we need to check type of it
                 - until stack is not empty and top of stack is not opening precedence with same type , we should add
                   top to result and pop
                 - at the end we pop ( cuz the current top is opening precedence )
                 */
                else if (symbol instanceof Precedence) {
                    if (((Precedence) symbol).getConditionType() == Type.OPENING)
                        stack.push((Operator) symbol);
                    else {
                        // parentheses :
                        if (((Precedence) symbol).getPrecedenceType() == PrecedenceType.PARENTHESES) {
                            while (!stack.isEmpty() && !Objects.equals(stack.top().getSymbols(), new OpeningParentheses().getSymbols())) {
                                result.add(stack.top().getSymbols());
                                stack.pop();
                            }
                            stack.pop();
                        }
                        // bracket :
                        else {
                            while (!stack.isEmpty() && !Objects.equals(stack.top().getSymbols(), new OpeningBracket().getSymbols())) {
                                result.add(stack.top().getSymbols());
                                stack.pop();
                            }
                            stack.pop();
                        }
                    }
                    i = i + symbol.getSymbols().length();
                } else {
                    Operator operator = (Operator) symbol;
                    // when operator is Minus and used as negative sign :
                    /*  1) when previous operator is opening precedence or binary operator
                        2) when it is at the first index of expression ( previous is null )
                    in this condition we do not pop element with greater priority
                     ( -3 -> (0-3) : when we consider parentheses , we can find the reason )
                     */
                    if ((operator instanceof Minus && (previousSymbol instanceof OpeningBracket
                            || previousSymbol instanceof OpeningParentheses || previousSymbol instanceof Binary))
                            || (operator instanceof Minus && previousSymbol == null)) {
                        result.add("0");
                        stack.push((Operator) symbol);
                        i = i + symbol.getSymbols().length();
                        previousSymbol = symbol;
                        continue;
                    }

                    // in other case we pop element of stack with greater priority :
                    while (!stack.isEmpty() && operator.getPriority() >= stack.top().getPriority() && stack.top().getPriority() > 0) {
                        result.add(stack.top().getSymbols());
                        stack.pop();
                    }

                    stack.push((Operator) symbol);
                    i = i + symbol.getSymbols().length();
                }
            }
            // when symbol is an operand we need to add it into result
            else {
                String operand = this.isOperand(expression, i);
                result.add(operand);
                i = i + operand.length();
            }

            // update previous symbol
            previousSymbol = symbol;
        }
        // Pop all the operators from the stack
        while (!stack.isEmpty()) {
            result.add(stack.pop().getSymbols());
        }
        return result;
    }

    private String isOperand(String expression, int firstIndexOfOperatorSymbol) {
        int index = firstIndexOfOperatorSymbol;
        StringBuilder operand = new StringBuilder();
        // numbers or "."
        while (((int) expression.charAt(index) >= 48 && (int) expression.charAt(index) <= 57) || String.valueOf(expression.charAt(index)).equals(".")) {
            operand.append(expression.charAt(index));
            index++;
            //for handling index out of bound error
            if (index > expression.length() - 1)
                break;
        }
        return operand.toString();
    }

    // calculate postfix ------------------------------------------------------------
    public String calculatePostfix(ArrayList<String> postfix) throws Exception {
        Validation validation = new Validation(validSymbols);
        LinkedStack<Number> stack = new LinkedStack<>();
        for (String s : postfix) {
            Operator o = validation.isOperator(s);
            if (o != null) {
                if (o instanceof Binary) {
                    Number b = stack.pop();
                    Number a = stack.pop();
                    Number answer = ((Binary) o).operate(a, b);
                    stack.push(answer);
                }
                // if o instance of Unary
                else {
                    Number answer = ((Unary) o).operate(stack.pop());
                    stack.push(answer);
                }
            }
            // when o is null it means s is number or constant
            else {
                Constant c = this.isConstant(s);
                // if s is constant
                if (c != null) {
                    stack.push(c.getValue());
                } else {
                    // if the number has "." -> double value
                    // ( throw exception for expression like "1.2.3" or "." )
                    if (s.contains("."))
                        stack.push(Double.parseDouble(s));
                    else
                        stack.push(Integer.parseInt(s));
                }
            }
        }
        if (!stack.isEmpty())
            return stack.pop().toString();
        else
            return null;
    }

    private Constant isConstant(String element) {
        Validation validation = new Validation(validSymbols);
        for (Constant c : validation.getConstants()) {
            int i = 0, j = 0;
            while (c.getSymbols().charAt(j) == element.charAt(i)) {
                i++;
                j++;
                // this symbol j-1 index is the last index
                if (j >= c.getSymbols().length())
                    break;
            }
            if (j == c.getSymbols().length()) {
                return c;
            }
        }
        return null;
    }

}
