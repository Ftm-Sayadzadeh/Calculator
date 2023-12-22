package Operation;

import models.dataStructures.LinkedStack;
import models.symbol.Symbol;
import models.symbol.constant.Constant;
import models.symbol.operator.Operator;
import models.symbol.operator.binary.Binary;
import models.symbol.operator.binary.Minus;
import models.symbol.operator.precedence.*;
import models.symbol.operator.unary.Unary;

import java.util.ArrayList;
import java.util.Objects;

public class Validation {
    private final ArrayList<Symbol> validSymbols;
    private final ArrayList<Precedence> opening = new ArrayList<>();
    private final ArrayList<Precedence> closing = new ArrayList<>();
    private final ArrayList<Constant> constants = new ArrayList<>();
    private final ArrayList<Operator> validOperator = new ArrayList<>();

    public Validation(ArrayList<Symbol> symbols) {
        validSymbols = symbols;
        //--------------------------------
        opening.add(new OpeningBracket());
        opening.add(new OpeningParentheses());
        //--------------------------------
        closing.add(new ClosingBracket());
        closing.add(new ClosingParentheses());
        //--------------------------------
        for (Symbol s : symbols) {
            if (s instanceof Constant)
                constants.add((Constant) s);
        }
        //--------------------------------
        for (Symbol s : symbols) {
            if (s instanceof Operator)
                validOperator.add((Operator) s);
        }
    }

    // getter ----------------------------------------------------

    public ArrayList<Symbol> getValidSymbols() {
        return validSymbols;
    }

    public ArrayList<Operator> getValidOperator() {
        return validOperator;
    }

    public ArrayList<Constant> getConstants() {
        return constants;
    }

    public ArrayList<Precedence> getOpening() {
        return opening;
    }

    public ArrayList<Precedence> getClosing() {
        return closing;
    }

    // METHODS : --------------------------------------------

    public void checkValidation(String expression) throws Exception {
        boolean matchPrecedence = matchPrecedence(expression);
        if (!matchPrecedence)
            throw new Exception("error : precedence is not matched ");
        //------------------------------
        boolean checkValidationOfElements = checkValidationOfElements(expression);
        if (!checkValidationOfElements)
            throw new Exception("error : validation of element");
        //------------------------------
        boolean checkValidationOfCondition = checkValidationOfCondition(expression);
        if (!checkValidationOfCondition)
            throw new Exception("error : validation of each element condition");
    }

    // check precedence -----------------------------------
    public boolean matchPrecedence(String expression) {
        LinkedStack<String> buffer = new LinkedStack<>();
        for (char c : expression.toCharArray()) {
            if (checkOpening(String.valueOf(c)) || checkClosing(String.valueOf(c))) {
                // If current char is opening , we need to push it in buffer
                if (checkOpening(String.valueOf(c))) {
                    buffer.push(String.valueOf(c));
                }
                // If current char is not opening , then If it is closing we need to check two condition
                else if (checkClosing(String.valueOf(c))) {
                    // buffer is empty means we do not have opening, but we have closing
                    if (buffer.isEmpty())
                        return false;
                    if (findIndexInClosingList(String.valueOf(c)) != findIndexInOpeningList(buffer.pop()))
                        return false;
                }
            }
        }
        // this expression is right
        if (buffer.isEmpty())
            return true;
            // when we have opening more than closing
        else
            return false;
    }

    private boolean checkOpening(String precedence) {
        boolean exist = false;
        for (Precedence e : opening) {
            if (Objects.equals(e.getSymbols(), precedence)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    private boolean checkClosing(String precedence) {
        boolean exist = false;
        for (Precedence e : closing) {
            if (Objects.equals(e.getSymbols(), precedence)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    private int findIndexInOpeningList(String c) {
        for (Precedence e : opening) {
            if (Objects.equals(e.getSymbols(), c)) {
                return opening.indexOf(e);
            }
        }
        return 0;
    }

    private int findIndexInClosingList(String c) {
        for (Precedence e : closing) {
            if (Objects.equals(e.getSymbols(), c)) {
                return closing.indexOf(e);
            }
        }
        return 0;
    }

    // check validation of operators and numbers ------------------------------
    public boolean checkValidationOfElements(String expression) {
        int i = 0;
        while (i < expression.length() - 1) {
            int checkOperator = isOperator(expression, i);
            /* if checkOperator function return i , it means this element is not an operator
            then we need to check if it is an operand or not
             */
            if (checkOperator == i) {
                int checkOperand = isOperand(expression, i);
                if (checkOperand == i)
                    return false;
                else
                    i = checkOperand;
            } else
                i = checkOperator;
        }
        return true;
    }

    private int isOperator(String expression, int firstIndexOfOperatorSymbol) {
        for (Symbol s : validSymbols) {
            int index = firstIndexOfOperatorSymbol;
            for (char c : s.getSymbols().toCharArray()) {
                if (c == expression.charAt(index))
                    index++;
            }
            // index - firstIndexOfOperatorSymbol = length of symbol
            // in this case it mean we can find it
            if ((index - firstIndexOfOperatorSymbol) == s.getSymbols().length())
                return index;

        }
        return firstIndexOfOperatorSymbol;
    }

    private int isOperand(String expression, int firstIndexOfOperand) {
        int index = firstIndexOfOperand;
        // numbers or "."
        while (((int) expression.charAt(index) >= 48 && (int) expression.charAt(index) <= 57) || String.valueOf(expression.charAt(index)).equals(".")) {
            index++;
            //for handling index out of bound error
            if (index > expression.length() - 1)
                break;
        }
        return index;
    }

    // check validation of each operator condition ------------------------------
    public boolean checkValidationOfCondition(String expression) {
        int i = 0;
        while (i <= expression.length() - 1) {
            Symbol symbol = isSymbol(expression, i);
            /* when the last char is binary or "[(" this while loop throw exception
                 so when we are at the last index we can not have binary
                 ( opening precedence was checked in matchPrecedence function )
                 */
            if (i == expression.length() - 1 && symbol != null) {
                if (symbol instanceof Binary)
                    return false;
                else
                    return true;
            }
            //------------------------------------------------------------------------
            // in other indexes , if this is operator :
            if (symbol != null) {
                // when symbol is constant
                if (symbol instanceof Constant) {
                    // then we should check condition in i + symbol.getSymbols().length() index
                    // cuz the time when the constant is last element of expression
                    if (i + symbol.getSymbols().length() < expression.length()) {
                        // new i element should not be opening precedence
                        if (this.checkOpening(String.valueOf(expression.charAt(i))))
                            return false;
                    }
                } else if (symbol instanceof Binary) {
                    if (!checkAfterBinaryOrOpeningPrecedence(expression, i + symbol.getSymbols().length()))
                        return false;
                } else if (symbol instanceof Unary) {
                    if (!checkAfterUnaryOrClosingPrecedence(expression, i + symbol.getSymbols().length()))
                        return false;
                } else if (symbol instanceof Precedence) {
                    if (((Precedence) symbol).getConditionType() == Type.OPENING) {
                        if (!checkAfterBinaryOrOpeningPrecedence(expression, i + symbol.getSymbols().length()))
                            return false;
                    } else {
                        if (!checkAfterUnaryOrClosingPrecedence(expression, i + symbol.getSymbols().length()))
                            return false;
                    }
                }
                i = i + symbol.getSymbols().length();
            } else {
                // if it is number , update the i ( + length of number )
                i = isOperand(expression, i);
                // cuz the time when the constant is last element of expression
                if (i < expression.length()) {
                    // new i element should not be opening precedence
                    if (this.checkOpening(String.valueOf(expression.charAt(i))))
                        return false;
                }
            }
        }
        return true;
    }

    public Symbol isSymbol(String expression, int index) {
        for (Symbol s : validSymbols) {
            int i = index;
            int j = 0;
            while (s.getSymbols().charAt(j) == expression.charAt(i)) {
                i++;
                j++;
                // this symbol j-1 index is the last index
                if (j >= s.getSymbols().length())
                    break;
            }
            if (j == s.getSymbols().length())
                return s;
        }
        return null;
    }

    private boolean isConstant(String expression, int index) {
        for (Constant c : constants) {
            int i = index;
            int j = 0;
            while (c.getSymbols().charAt(j) == expression.charAt(i)) {
                i++;
                j++;
                // this symbol j-1 index is the last index
                if (j >= c.getSymbols().length())
                    break;
            }
            if (j == c.getSymbols().length()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkAfterBinaryOrOpeningPrecedence(String expression, int indexOfNextElement) {
        //1) check if this symbol is constant
        boolean isConstant = isConstant(expression, indexOfNextElement);
        //2) check if it is operand
        boolean isOperand = false;
        int updateIndex = isOperand(expression, indexOfNextElement);
        if (!(indexOfNextElement == updateIndex)) {
            isOperand = true;
        }
        //3) check if it is opening precedence
        boolean isOpeningPrecedence = false;
        for (Precedence p : opening) {
            if (String.valueOf(expression.charAt(indexOfNextElement)).equals(p.getSymbols())) {
                isOpeningPrecedence = true;
                break;
            }
        }
        //4) check if it is minus is negative sign
        boolean isNegativeSign = false;
        Operator operator = this.isOperator(String.valueOf(expression.charAt(indexOfNextElement)));
        if (operator instanceof Minus)
            isNegativeSign = true;
        // if one of this condition is true , it is valid
        return isConstant || isOpeningPrecedence || isOperand || isNegativeSign;
    }

    private boolean checkAfterUnaryOrClosingPrecedence(String expression, int index) {
        // 1) if index-1 is the last index of expression
        // return true in this case cuz the other condition throw index out of bound exception
        if (index >= expression.length())
            return true;
        // 2) check if it is binary or unary operator
        boolean isOperator = false;
        Symbol s = isSymbol(expression, index);
        if (s != null) {
            if (s instanceof Binary || s instanceof Unary)
                isOperator = true;
        }
        // 3) check if it is closing precedence
        boolean isClosingPrecedence = false;
        for (Precedence p : closing) {
            if (String.valueOf(expression.charAt(index)).equals(p.getSymbols())) {
                isClosingPrecedence = true;
                break;
            }
        }
        // if one of this condition is true , it is valid
        return isOperator || isClosingPrecedence;
    }

    public Operator isOperator(String name) {
        for (Operator o : validOperator) {
            int i = 0, j = 0;
            while (o.getSymbols().charAt(j) == name.charAt(i)) {
                i++;
                j++;
                // this operator j-1 index is the last index
                if (j >= o.getSymbols().length())
                    break;
            }
            if (j == o.getSymbols().length())
                return o;
        }
        return null;
    }

}
