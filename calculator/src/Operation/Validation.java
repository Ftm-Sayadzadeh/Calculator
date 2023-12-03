package Operation;

import models.dataStructures.LinkedStack;
import models.symbol.Symbol;
import models.symbol.constant.Constant;
import models.symbol.operator.Operator;
import models.symbol.operator.precedence.*;

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
}
