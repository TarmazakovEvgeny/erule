package ru.mephi.translators.dfa;


public class Rule {
    private String from;
    private String in;
    private String on;

    public Rule() {
    }

    public Rule(String from, String in, String on) {
        this.from = from;
        this.in = in;
        this.on = on;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
