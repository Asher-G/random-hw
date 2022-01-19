package edu.iastate.cs228.hw3;

/**
 *
 * @author Asher Gust
 *
 */

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class InfixExpression {

    private static String postfixExpression;
    private static int rank;
    private static Deque<Operator> operatorStack;

    private static void outputHigherOrEqual(Operator op){
        if (operatorStack.isEmpty()) {
            op.adjustPrecedence();
            operatorStack.addFirst(op);
        } else{
            if (op.getSymbol().equals(")") && operatorStack.peek().getSymbol().equals("(")) throw new IllegalArgumentException("Error: no subexpression detected ()");
            while (!operatorStack.isEmpty() && operatorStack.peek().getPrecedence() >= op.getPrecedence()){
                postfixExpression += operatorStack.pop().getSymbol() + " ";
                if (operatorStack.isEmpty() && op.getSymbol().equals(")")) throw new IllegalArgumentException("Error: no opening parenthesis detected");
            }
            if (op.getSymbol().equals(")")){
                if (operatorStack.peek().getSymbol().equals("(")) operatorStack.pop();
                else throw new IllegalArgumentException("Error: no opening parenthesis detected");
            } else {
                op.adjustPrecedence();
                operatorStack.addFirst(op);
            }
        }
    }

    public static String postfix(String infix) {
        postfixExpression = "";
        rank = 0;
        operatorStack = new ArrayDeque<Operator>();
        Scanner scnr = new Scanner(infix);
        while (scnr.hasNext()) {
            String character = scnr.next();
            if (isOperand(character)) {
                postfixExpression += character + " ";
                rank++;
            } else {
                try {
                    Operator op = new Operator(character);
                    if (!character.equals("(") && !character.equals(")") && rank == 0) return "Error: too many operators (" + character + ")";
                    outputHigherOrEqual(op);
                } catch (IllegalArgumentException e) {
                    return e.getLocalizedMessage();
                }
                if (!character.equals("(") && !character.equals(")")) rank--;
            }
            if (rank > 1) return "Error: too many operands (" + character + ")";
            if (rank < 0) return "Error: too many operators (" + character + ")";
        }
        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek().getSymbol().equals("(")) return "Error: no closing parenthesis detected";
            postfixExpression += operatorStack.pop().getSymbol() + " ";
        }
        if (postfixExpression.length() > 0) {
            postfixExpression = postfixExpression.substring(0, postfixExpression.length() - 1);
        }
        return postfixExpression;
    }

    private static boolean isOperand(String s) {
        if (s == null) return false;
        int length = s.length();
        if (length == 0)  return false;
        int i = 0;
        if (s.charAt(0) == '-') {
            if (length == 1) return false;
            i = 1;
        }
        for (; i < length; i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9')  return false;
        }
        return true;
    }
}
