package edu.iastate.cs228.hw3;

/**
 *
 * @author Asher Gust
 *
 */
public class Operator {

    private int precedence;
    private String symbol;

    public Operator(String symbol){
        this.symbol = symbol;
        if (symbol.equals("+") || symbol.equals("-")) precedence = 1;
        else if (symbol.equals("*") || symbol.equals("/") || symbol.equals("%")) precedence = 2;
        else if (symbol.equals("^")) precedence = 4;
        else if (symbol.equals("(")) precedence = 5;
        else if (symbol.equals(")")) precedence = 0;
        else throw new IllegalArgumentException("Error: invalid character (" + symbol + ")");
    }

    public int getPrecedence(){
        return precedence;
    }
    public String getSymbol(){
        return symbol;
    }
    public void adjustPrecedence(){
        if (precedence == 4) precedence = 3;
        else if (precedence == 5) precedence = -1;
    }
}
