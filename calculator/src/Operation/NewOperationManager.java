package Operation;

import models.symbol.Symbol;
import models.symbol.constant.Constant;
import models.symbol.operator.OperatorType;
import models.symbol.operator.binary.NewBinaryOperator;
import models.symbol.operator.unary.NewUnaryOperator;

public class NewOperationManager {
    public Calculation calculation;
    public OperatorType operatorType;

    // METHODS : -----------------------------------------------------------
    public NewOperationManager(Calculation calculation , int operatorTypeNumber){
        this.calculation = calculation;
        if(operatorTypeNumber == 1)
            this.operatorType = OperatorType.UNARY;
        else if (operatorTypeNumber == 2)
            this.operatorType = OperatorType.BINARY;
    }
    public void addNewOperation(String symbol , String formula) throws Exception {
        // the new symbol can not be duplicated
        if(!isExist(symbol)){
            if(validationOfFormula(formula)){
                if(operatorType == OperatorType.BINARY){
                    NewBinaryOperator newBinaryOperator = new NewBinaryOperator(symbol , formula , calculation);
                    calculation.getValidSymbols().add(newBinaryOperator);
                }
                else if(operatorType == OperatorType.UNARY){
                    NewUnaryOperator newUnaryOperator = new NewUnaryOperator(symbol , formula , calculation);
                    calculation.getValidSymbols().add(newUnaryOperator);
                }
            }
        }
        else{
            throw new Exception("error : duplicated symbol");
        }
    }
    private boolean isExist(String symbol){
        for(Symbol s : calculation.getValidSymbols()){
            if(s.getSymbols().equals(symbol)){
                return true;
            }
        }
        // symbol of element in new operator expression in my code is "a and b" so this symbol can not be "a and b"
        if(symbol.equals("a") || symbol.equals("b")){
            return true;
        }
        return false;
    }
    private boolean validationOfFormula(String expression){
        boolean validationOfFormula = true;

        // in Unary case :
        if(operatorType == OperatorType.UNARY) {
            if(expression.contains("b"))
                return false;
            // cuz "a" is number , we can consider it as a constant (numbers and constant have same validation of condition)
            Constant a = new Constant("a" , 1);
            calculation.getValidSymbols().add(a);

            Validation validation = new Validation(calculation.getValidSymbols());
            //check validation of expression :
            try {
                validation.checkValidation(expression);
            } catch (Exception e) {
                System.out.println("error : new unary operator expression is invalid");
                validationOfFormula = false;
            }
            //-----------------------------------------------
            calculation.getValidSymbols().remove(a);
        }

        // in Binary case :
        else if(operatorType == OperatorType.BINARY){
            // cuz "a" is number when can consider it as a constant (numbers and constant have same validation of condition)
            Constant a = new Constant("a" , 1);
            Constant b = new Constant("b" , 1);
            calculation.getValidSymbols().add(a);
            calculation.getValidSymbols().add(b);

            Validation validation = new Validation(calculation.getValidSymbols());
            //check validation of expression :
            try {
                validation.checkValidation(expression);
            } catch (Exception e) {
                System.out.println("error : new binary operator expression is invalid");
                validationOfFormula = false;
            }
            //-----------------------------------------------
            calculation.getValidSymbols().remove(a);
            calculation.getValidSymbols().remove(b);
        }
        return validationOfFormula;
    }
}