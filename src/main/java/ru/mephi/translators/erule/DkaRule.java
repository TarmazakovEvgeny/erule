package ru.mephi.translators.erule;


public class DkaRule {

    private String from;
    private String to;
    private String on;

    public DkaRule() {
    }

    public DkaRule(String from, String to, String on) {
        this.from = from;
        this.to = to;
        this.on = on;
    }

    @Override
    public String toString() {
        return "DkaRule{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", on='" + on + '\'' +
                '}';
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }
}
