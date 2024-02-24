package org.kurodev.graph.calculator;

import org.kurodev.graph.calculator.sanitisation.DefaultSanitizers;
import org.kurodev.graph.calculator.sanitisation.Sanitizer;

import java.math.BigDecimal;
import java.util.*;

public class Calculator {
    private final Map<String, BigDecimal> variables = new HashMap<>();
    private final List<Operation> operations = new ArrayList<>();

    private final List<Sanitizer> sanitizers = new ArrayList<>();

    public Calculator() {
        operations.addAll(List.of(DefaultOperations.values()));
        sanitizers.addAll(List.of(DefaultSanitizers.values()));

    }

    public Map<String, BigDecimal> getVariables() {
        return variables;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public BigDecimal evaluate(String expression) throws RuntimeException {
        for (Sanitizer sanitizer : sanitizers) {
            expression = sanitizer.sanitize(expression);
        }
        String postfix = infixToPostfix(expression);
        return evaluatePostfix(postfix);
    }

    private String infixToPostfix(String infix) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        for (char c : infix.toCharArray()) {
            if (Character.isDigit(c) || Character.isAlphabetic(c) || c == '.') {
                postfix.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop(); // remove the '(' from the stack
                }
            } else {
                while (!stack.isEmpty() && DefaultOperations.of(stack.peek()).getPrecedence() >= DefaultOperations.of(c).getPrecedence()) {
                    postfix.append(stack.pop());
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }
        return postfix.toString();
    }

    private BigDecimal evaluatePostfix(String postfix) {
        Stack<BigDecimal> stack = new Stack<>();
        char[] charArray = postfix.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (Character.isDigit(c)) {
                StringBuilder digit = new StringBuilder(String.valueOf(c));
                while (i < charArray.length - 1) {
                    char c2 = charArray[++i];
                    if (c2 == '.' || Character.isDigit(c2)) {
                        digit.append(c2);
                    } else {
                        break;
                    }
                }
                i--;
                stack.push(new BigDecimal(digit.toString()));
            } else if (Character.isAlphabetic(c)) {
                BigDecimal value = variables.getOrDefault(Character.toString(c), BigDecimal.ZERO);
                stack.push(value);
            } else {
                BigDecimal b = stack.pop();
                BigDecimal a = stack.pop();
                for (Operation operation : operations) {
                    if (operation.getOperator() == c) {
                        stack.push(operation.conclude(a, b));
                        break;
                    }
                }
            }
        }
        return stack.pop();
    }

    public void assign(String variable, String expression) {
        BigDecimal result = evaluate(expression);
        variables.put(variable, result);
    }

    public BigDecimal getVariable(String variable) {
        return variables.get(variable);
    }
}
