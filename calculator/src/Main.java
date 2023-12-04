import Operation.Calculation;
import Operation.NewOperationManager;
import models.symbol.Symbol;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Calculation c = new Calculation();

        while (true) {
            System.out.println("""
                    1 - calculate
                    2 - add new operation
                    3 - show available operations
                    0 - end
                    """);
            int command = sc.nextInt();

            switch (command) {
                case 0 -> System.exit(0);
                case 1 -> {
                    sc.nextLine();
                    try {
                        System.out.println(c.calculate(sc.nextLine()));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.println("1 - Unary\n2 - Binary");
                    int operatorTypeNumber = sc.nextInt();
                    System.out.println("first enter the name then enter the formula");
                    System.out.println("* NOTICE : a and b are consider as operand so the formula can contain them and operator");
                    NewOperationManager newOperationManager = new NewOperationManager(c, operatorTypeNumber);
                    try {
                        sc.nextLine();
                        String expression = sc.nextLine();
                        String formula = sc.nextLine();
                        newOperationManager.addNewOperation(expression , formula);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                case 3 -> {
                    for (Symbol s : c.getValidSymbols())
                        System.out.print(s.getSymbols() + " ");
                }
                default -> {
                    System.out.println("Enter correct number!");
                }
            }
        }
    }
}