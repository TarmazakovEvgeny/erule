package ru.mephi.translators.dfa;

import java.util.ArrayList;


public class DkaState {

    private String name;

    private ArrayList<String> states;

    private Boolean finished;

    public DkaState() {
        states = new ArrayList<String>();
    }

    public DkaState(ArrayList<String> states, String name) {
        this.states = states;
        this.name = name;
    }

    public void putState(String state) {
        if (states == null) {
            states = new ArrayList<String>();
        }
        states.add(state);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getStates() {
        return states;
    }

    public void setStates(ArrayList<String> states) {
        this.states = states;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
