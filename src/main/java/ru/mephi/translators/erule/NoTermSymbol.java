package ru.mephi.translators.erule;

public class NoTermSymbol {

    private String symbol;
    private boolean isContainsE;

    public NoTermSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public boolean isContainsE() {
        return isContainsE;
    }

    public void setContainsE(boolean containsE) {
        isContainsE = containsE;
    }
}
