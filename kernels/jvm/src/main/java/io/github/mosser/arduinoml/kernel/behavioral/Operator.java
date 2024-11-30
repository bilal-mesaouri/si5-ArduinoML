package io.github.mosser.arduinoml.kernel.behavioral;

public enum Operator {
    AND("&&"),
    OR("||");

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
    
}