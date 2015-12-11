package ru.mephi.translators.erule;


public class Rule {
    private NoTermSymbol from;
    private String in;
    private boolean isChain;

    public Rule() {
    }

    public Rule(NoTermSymbol from, String in) {
        this.from = from;
        this.in = in;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public NoTermSymbol getFrom() {
        return from;
    }

    public void setFrom(NoTermSymbol from) {
        this.from = from;
    }

    public boolean isChain() {
        return isChain;
    }

    public void setChain(boolean chain) {
        isChain = chain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rule)) return false;

        Rule rule = (Rule) o;

        if (getFrom() != null ? !getFrom().getSymbol().equals(rule.getFrom().getSymbol()) : rule.getFrom() != null)
            return false;
        return !(getIn() != null ? !getIn().equals(rule.getIn()) : rule.getIn() != null);

    }

    @Override
    public String toString() {
        return from.getSymbol() + " -> " + in;
    }
}
