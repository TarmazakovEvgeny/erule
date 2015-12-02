package ru.mephi.translators.dfa;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static Integer n;
    private static Integer m;
    private static Integer k;
    private static Integer l;

    private static ArrayList<String> stateList = new ArrayList<String>();
    private static ArrayList<String> finalStateList = new ArrayList<String>();

    private static String startSymbol;

    private static ArrayList<String> alphabetList = new ArrayList<String>();

    private static ArrayList<Rule> ruleList = new ArrayList<Rule>();

    private static ArrayList<DkaState> dkaStateList = new ArrayList<DkaState>();
    private static ArrayList<DkaRule> dkaRuleList = new ArrayList<DkaRule>();

    private static boolean active = true;

    public static void main(String[] arguments) {
//        n — количество состояний
//        m — количество конечных состояний
//        k — количество символов в алфавите
//        l — количество переходов
//        A B... — состояния, перечисленные через пробел
//        N M... — конечные состояния, перечисленные через пробел
//        S — стартовый символ
//        a b c d ... — алфавит
//        A B a — правила


        readDataFromConsole(stateList, finalStateList, alphabetList, ruleList);
        //initTestData();

//        for (String state : stateList) {
//            for (String alph : alphabetList) {
//                dkaStateList.add(getDkaStateFromRule(ruleList, state, alph));
//                stateList.add("V");
//            }
//        }

        DkaState dkaSt = new DkaState();
        dkaSt.setName("A");
        dkaSt.putState(startSymbol);
        dkaStateList.add(dkaSt);
        char a = 'B';
        while (active) {
            for (int i = 0; i < Math.pow(2, n); i++) {
                if (i < dkaStateList.size()) {
                    for (String alph : alphabetList) {
                        DkaState dkaStateFromRule = getDkaStateFromRule(ruleList, dkaStateList.get(i).getStates(),
                                alph);
                        if (findAndGetDkaState(dkaStateFromRule) != null) {
                            dkaStateFromRule = findAndGetDkaState(dkaStateFromRule);
                            DkaRule dkaRule = new DkaRule(dkaStateList.get(i).getName(), dkaStateFromRule.getName(), alph);
                            dkaRuleList.add(dkaRule);
                        } else {
                            if (dkaStateFromRule.getStates().size() != 0) {
                                dkaStateFromRule.setName(String.valueOf(a));
                                dkaStateList.add(dkaStateFromRule);
                                DkaRule dkaRule = new DkaRule(dkaStateList.get(i).getName(), dkaStateFromRule.getName(), alph);
                                dkaRuleList.add(dkaRule);
                                a++;
                            }
                        }
                    }
                } else {
                    active = false;
                    break;
                }
            }
        }
        makeDkaStatesFinish();
        printResult();
        System.out.println("Hello, world");
    }

    private static DkaState getDkaStateFromRule(ArrayList<Rule> ruleList, String state, String alph) {
        DkaState dkaState = new DkaState();
        for (Rule rule : ruleList) {
            if (rule.getFrom().equals(state) && rule.getOn().equals(alph)) {
                if (dkaState.getStates() != null) {
                    if (!dkaState.getStates().contains(rule.getIn())) {
                        dkaState.putState(rule.getIn());
                    }
                }

            }
        }
        return dkaState;
    }

    private static DkaState getDkaStateFromRule(ArrayList<Rule> ruleList, ArrayList<String> states, String alph) {
        DkaState dkaState = new DkaState();
        for (String state : states) {
            for (Rule rule : ruleList) {
                if (rule.getFrom().equals(state) && rule.getOn().equals(alph)) {
                    if (!dkaState.getStates().contains(rule.getIn())) {
                        dkaState.putState(rule.getIn());
                    }
                }
            }
        }
        return dkaState;
    }

    private static void makeDkaStatesFinish() {
        for (DkaState dkaState : dkaStateList) {
            for (String finalState : finalStateList) {
                for (String s : dkaState.getStates()) {
                    if (s.equals(finalState)) {
                        dkaState.setFinished(true);
                    }
                }
            }
        }
    }

    private static void printResult() {
        for (DkaState dkaState : dkaStateList) {
            System.out.println(dkaState);
        }
        for (DkaRule dkaRule : dkaRuleList) {
            System.out.println(dkaRule);
        }
    }

    private static DkaState findAndGetDkaState(DkaState o) {
        for (DkaState dkaState : dkaStateList) {
            if (dkaState.getStates().size() == o.getStates().size()) {
                int k = 0;
                for (String s : o.getStates()) {
                    if (dkaState.getStates().contains(s)) {
                        k++;
                    }
                }
                if (k == dkaState.getStates().size()) {
                    return dkaState;
                }
            }
        }
        return null;
    }

    private static void readDataFromConsole(ArrayList<String> stateList, ArrayList<String> finalStateList, ArrayList<String> alphabet, ArrayList<Rule> ruleList) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите колличество состояний: ");
        n = sc.nextInt();

        System.out.print("Введите количество конечных состояний: ");
        m = sc.nextInt();

        System.out.print("Введите количество символов в алфавите: ");
        k = sc.nextInt();

        System.out.print("Введите колличество переходов: ");
        l = sc.nextInt();


        System.out.print("Введите состояния: ");
        sc = new Scanner(System.in);
        String states = sc.nextLine();
        for (String s : states.split(" ")) {
            stateList.add(s);
        }

        System.out.print("Введите конечные состояния: ");
        sc = new Scanner(System.in);
        states = sc.nextLine();
        for (String s : states.split(" ")) {
            finalStateList.add(s);
        }

        System.out.print("Введите стартовый символ: ");
        startSymbol = sc.next();

        System.out.print("Введите алфавит: ");
        sc = new Scanner(System.in);
        states = sc.nextLine();
        for (String s : states.split(" ")) {
            alphabet.add(s);
        }

        System.out.println("Введите правила: ");
        for (int i = 0; i < l; i++) {
            sc = new Scanner(System.in);
            states = sc.nextLine();
            Rule rule = new Rule();
            ArrayList<String> arr = new ArrayList<String>();
            for (String s : states.split(" ")) {
                arr.add(s);
            }
            rule.setFrom(arr.get(0));
            rule.setIn(arr.get(1));
            rule.setOn(arr.get(2));
            ruleList.add(rule);
        }
    }

    private static void initTestData() {
        n = 8;
        m = 2;
        k = 3;
        l = 15;
        for (int i = 0; i < n; i++) {
            stateList.add("q" + i);
        }
        finalStateList.add("q0");
        finalStateList.add("q6");

        startSymbol = "q0";

        alphabetList.add("a");
        alphabetList.add("b");
        alphabetList.add("c");

        Rule rule = new Rule("q0", "q1", "a");
        ruleList.add(rule);
        rule = new Rule("q0", "q4", "a");
        ruleList.add(rule);
        rule = new Rule("q0", "q5", "a");
        ruleList.add(rule);
        rule = new Rule("q0", "q6", "a");
        ruleList.add(rule);
        rule = new Rule("q0", "q4", "b");
        ruleList.add(rule);
        rule = new Rule("q0", "q3", "c");
        ruleList.add(rule);
        rule = new Rule("q0", "q5", "c");
        ruleList.add(rule);
        rule = new Rule("q1", "q2", "b");
        ruleList.add(rule);
        rule = new Rule("q2", "q0", "a");
        ruleList.add(rule);
        rule = new Rule("q2", "q4", "a");
        ruleList.add(rule);
        rule = new Rule("q3", "q0", "a");
        ruleList.add(rule);
        rule = new Rule("q3", "q4", "a");
        ruleList.add(rule);
        rule = new Rule("q4", "q4", "a");
        ruleList.add(rule);
        rule = new Rule("q4", "q5", "a");
        ruleList.add(rule);
        rule = new Rule("q4", "q6", "a");
        ruleList.add(rule);
        rule = new Rule("q4", "q4", "b");
        ruleList.add(rule);
        rule = new Rule("q4", "q5", "c");
        ruleList.add(rule);
        rule = new Rule("q5", "q5", "a");
        ruleList.add(rule);
        rule = new Rule("q5", "q6", "a");
        ruleList.add(rule);
        rule = new Rule("q5", "q5", "c");
        ruleList.add(rule);
        rule = new Rule("q6", "q7", "b");
        ruleList.add(rule);
        rule = new Rule("q7", "q5", "a");
        ruleList.add(rule);
        rule = new Rule("q7", "q4", "a");
        ruleList.add(rule);
        rule = new Rule("q7", "q4", "b");
        ruleList.add(rule);
        rule = new Rule("q7", "q5", "c");
        ruleList.add(rule);

    }

    private static void readFile() {
        String line;
        try {
            InputStream fis = new FileInputStream("/home/evgeny/IdeaProjects/dfa/src/main/java/ru/mephi/translators/dfa/data.txt");
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                Rule rule = new Rule();
                ArrayList<String> arr = new ArrayList<String>();
                for (String s : line.split(" ")) {
                    arr.add(s);
                }
                rule.setFrom(arr.get(0));
                rule.setIn(arr.get(1));
                rule.setOn(arr.get(2));
                ruleList.add(rule);
            }
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }
}
