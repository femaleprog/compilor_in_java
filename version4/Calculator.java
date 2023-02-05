import java.lang.ProcessBuilder.Redirect.Type;
import java.util.*;
import java.util.Stack;
import java.io.*;

class Calculator {
    public static double calculate(String operations) {
        Stack<Double> values = new Stack<Double>();
        Stack<Character> operators = new Stack<Character>();

        for (int i = 0; i < operations.length(); i++) {
            char currentChar = operations.charAt(i);

            if (currentChar == ' ') {
                continue;
            }

            if (Character.isDigit(currentChar)) {
                String number = "";
                while (i < operations.length() && Character.isDigit(operations.charAt(i))) {
                    number += operations.charAt(i);
                    i++;
                }
                i--;
                values.push(Double.parseDouble(number));
            } else if (currentChar == '(') {
                operators.push(currentChar);
            } else if (currentChar == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop();
            } else if (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/') {
                while (!operators.empty() && hasPrecedence(currentChar, operators.peek())) {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(currentChar);
            }
        }
        while (!operators.empty()) {

        
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    public static boolean hasPrecedence(char operator1, char operator2) {
        if (operator2 == '(' || operator2 == ')') {
            return false;
        }
        if ((operator1 == '*' || operator1 == '/') && (operator2 == '+' || operator2 == '-')) {
            return false;
        } else {
            return true;
        }
    }

    public static double applyOperator(char operator, double number1, double number2) {
        switch (operator) {
            case '+':
                return number1 + number2;
            case '-':
                return number2 - number1;
            case '*':
                return number1 * number2;
            case '/':
                if (number1 == 0) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return number2 / number1;
        }
        return 0;
    }
}