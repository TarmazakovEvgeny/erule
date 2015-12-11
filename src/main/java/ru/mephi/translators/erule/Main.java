package ru.mephi.translators.erule;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static Integer n;
    private static Integer k;
    private static Integer l;

    private static ArrayList<NoTermSymbol> noTermSymbolList = new ArrayList<NoTermSymbol>();

    private static NoTermSymbol startSymbol;

    private static ArrayList<String> alphabetList = new ArrayList<String>();

    private static ArrayList<Rule> oldRuleList = new ArrayList<Rule>();
    private static ArrayList<Rule> ruleList = new ArrayList<Rule>();
    private static ArrayList<Rule> newRuleList = new ArrayList<Rule>();

    private static ArrayList<DkaState> dkaStateList = new ArrayList<DkaState>();
    private static ArrayList<DkaRule> dkaRuleList = new ArrayList<DkaRule>();

    private static boolean active = true;

    public static void main(String[] arguments) {

//        n — количество нетерминальных символов
//        k — количество терминальных символов
//        l — количество правил
//        A B... — нетерминалы, перечисленные через пробел
//        S — стартовый символ
//        a b c d ... — алфавит
//        A B a — правила
//        \(\epsilon\) представляется как спец символ #
//        readDataFromConsole(noTermSymbolList, alphabetList, ruleList);
        initTestDataForERule();
        //initTestDataForChainRule();
        makeNset();
        removeERule();
        //removeChainRule(); // удаление цепных правил
        printResult();

        System.out.println("Hello, world");
    }

    private static void removeChainRule() {
        for (Rule rule : ruleList) {
            for (NoTermSymbol noTermSymbol : noTermSymbolList) {
                if (rule.getIn().equals(noTermSymbol.getSymbol())) {
                    rule.setChain(true);
                }
            }
        }
        for (Rule rule : ruleList) {
            if (!rule.isChain()) {
                newRuleList.add(rule);
            }
        }
        for (int i = 0; i < ruleList.size(); i++) {
            if (ruleList.get(i).isChain()) {
                for (int j = 0; j < ruleList.size(); j++) {
                    if (ruleList.get(i).getIn().equals(ruleList.get(j).getFrom().getSymbol())) {
                        Rule rule = new Rule(ruleList.get(i).getFrom(), ruleList.get(j).getIn());
                        for (NoTermSymbol noTermSymbol : noTermSymbolList) {
                            if (ruleList.get(j).getIn().equals(noTermSymbol.getSymbol())) {
                                rule.setChain(true);
                            }
                        }
                        ruleList.add(rule);
                    }
                }
            }
        }
        for (int i = 0; i < ruleList.size(); i++) {
            if (ruleList.get(i).isChain()) {
                ruleList.remove(i);
            }
        }
        for (int i = 0; i < ruleList.size(); i++) {
            for (int j = 0; j < ruleList.size(); j++) {
                if (i != j) {
                    if (ruleList.get(i).equals(ruleList.get(j))) {
                        ruleList.remove(j);
                    }
                }
            }
        }

    }


    private static void removeERule() {
        oldRuleList = ruleList;
        for (Rule rule : ruleList) {
            int symbolCount = rule.getIn().length();
            ArrayList<char[]> combinations = getCombinations(symbolCount);
            ArrayList<char[]> newCombinations = new ArrayList<char[]>();
            char[] ruleIn = rule.getIn().toCharArray();
            ArrayList<Integer> removeNumbers = new ArrayList<Integer>();
            for (NoTermSymbol noTermSymbol : noTermSymbolList) {
                for (int i = 0; i < ruleIn.length; i++) {
                    if (ruleIn[i] == noTermSymbol.getSymbol().toCharArray()[0]) {
                        removeNumbers.add(i);
                    }
                }
            }
            combinations.remove(0);

//            ArrayList<char[]> combinations


            //считаем новые комбинации
            for (int i = 0; i < combinations.size(); i++) {
                for (int j = 0; j < combinations.get(i).length; j++) {
                    if (combinations.get(i)[j] == '1') {
                        int countEquals = 0;
                        for (NoTermSymbol noTermSymbol : noTermSymbolList) {
                            if (ruleIn[j] == noTermSymbol.getSymbol().toCharArray()[0]) {
                                countEquals++;
                            }
                        }
                        if (countEquals == 0) {
                            combinations.get(i)[j] = 'd';
                        }
                    }

                }
            }

            //переносим новые комбинации в новый массив
            for (int i = 0; i < combinations.size(); i++) {
                int count = 0;
                for (int j = 0; j < combinations.get(i).length; j++) {
                    if (combinations.get(i)[j] != 'd') {
                        count++;
                    }
                }
                if (count == 4) {
                    newCombinations.add(combinations.get(i));
                }
            }

            // добавляем новые правила в соответсвии с новыми комбинациями
            for (int i = 0; i < newCombinations.size(); i++) {
                char[] newRuleChar = ruleIn;
                for (int j = 0; j < newCombinations.get(i).length; j++) {
                    if (newCombinations.get(i)[j] == '1') {
                        newRuleChar[j] = 'X';
                    }
                }
                String newRuleString = String.valueOf(newRuleChar);
                newRuleString.replaceAll("X","");
                Rule newRule = new Rule(rule.getFrom(), newRuleString);
                newRuleList.add(newRule);
            }
            //counter += getCounterSymbolInRule(rule, noTermSymbol);
//                while (newRule.getIn().contains(noTermSymbol.getSymbol())) {
//
//                    StringBuffer stringBuffer = new StringBuffer(newRule.getIn());
//                    int num = stringBuffer.indexOf(noTermSymbol.getSymbol());
//                    char c = 0;
//                    stringBuffer.setCharAt(num, c);
//                    ///////////////////////////////////////////////////////////////////////////
//
//                    char[] b = newRule.getIn().toCharArray();
//                    ArrayList<String> ruleSymbols = new ArrayList<String>();
//                    for (int i = 0; i < b.length; i++) {
//                        ruleSymbols.add(String.valueOf(b[i]));
//                    }
//
//                    ///////////////////////////////////////////////////////////////////////////
//                    newRule.setIn(stringBuffer.toString());
//                    newRuleList.add(new Rule(newRule.getFrom(), newRule.getIn()));
//                }


            for (int i = 0; i < ruleList.size(); i++) {
                if (ruleList.get(i).getIn().equals("#")) {
                    ruleList.remove(i);
                }
            }

            for (int i = 0; i < newRuleList.size(); i++) {
                ruleList.add(newRuleList.get(i));
            }

        }
    }

    public static char[] remove(char[] symbols, int index) {
        for (int i = 0; i < symbols.length; i++) {
            if (i == index) {
                char[] copy = new char[symbols.length - 1];
                System.arraycopy(symbols, 0, copy, 0, i);
                System.arraycopy(symbols, i + 1, copy, i, symbols.length - i - 1);
                return copy;
            }
        }
        return symbols;
    }


    private static ArrayList<char[]> getCombinations(int symbolCount) {
        int n = (int) Math.pow(2, symbolCount);
        ArrayList<char[]> removeNum = new ArrayList<char[]>();
        for (int i = 0; i < n; i++) {
            removeNum.add(binarNumber(i, symbolCount).toCharArray());
        }
        return removeNum;
    }

    private static String binarNumber(int i, int k) {
        Integer b;
        String s = "";
        while (i != 0) {
            b = i % 2;
            s = b.toString().concat(s);
            i = i / 2;
        }
        while (s.length() < k) {
            s = "0".concat(s);
        }
        return s;
    }

    private static int getCounterSymbolInRule(Rule rule, NoTermSymbol noTermSymbol) {
        int counter = 0;
        String str = rule.getIn();
        String substr = noTermSymbol.getSymbol();
        Pattern p = Pattern.compile(substr);
        Matcher m = p.matcher(str);
        while (m.find()) {
            counter++;
        }
        return counter;
    }

    private static void makeNset() {
        //make N0set
        for (Rule rule : ruleList) {
            if (rule.getIn().contains("#")) {
                for (NoTermSymbol noTermSymbol : noTermSymbolList) {
                    if (noTermSymbol.getSymbol().equals(rule.getFrom().getSymbol())) {
                        noTermSymbol.setContainsE(true);
                    }
                }
            }
        }
        for (Rule rule : ruleList) {
            if (!rule.getFrom().isContainsE()) {
                for (NoTermSymbol noTermSymbol : noTermSymbolList) {
                    if (rule.getIn().contains(noTermSymbol.getSymbol()) && noTermSymbol.isContainsE()) {
                        rule.getFrom().setContainsE(true);
                    }
                }
            }
        }
    }

    private static void printResult() {
        for (Rule rule : ruleList) {
            if (rule.getFrom().getSymbol().equals(startSymbol.getSymbol())) {
                System.out.println(rule.toString());
            }
        }
        for (NoTermSymbol noTermSymbol : noTermSymbolList) {
            for (Rule rule : ruleList) {
                if (rule.getFrom().getSymbol().equals(noTermSymbol.getSymbol())) {
                    System.out.println(rule.toString());
                }
            }
        }
    }

    private static void readDataFromConsole(ArrayList<NoTermSymbol> noTermSymbolList, ArrayList<String> alphabet, ArrayList<Rule> ruleList) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Введите колличество нетерминальных символов: ");
        n = sc.nextInt();

        System.out.print("Введите количество терминальных символов: ");
        k = sc.nextInt();

        System.out.print("Введите колличество правил: ");
        l = sc.nextInt();

        System.out.print("Введите нетерминальные символы: ");
        sc = new Scanner(System.in);
        String states = sc.nextLine();
        for (String s : states.split(" ")) {
            NoTermSymbol noTermSymbol = new NoTermSymbol(s);
            noTermSymbolList.add(noTermSymbol);
        }

        System.out.print("Введите стартовый символ: ");
        startSymbol = new NoTermSymbol(sc.next());

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
            NoTermSymbol noTermSymbol = new NoTermSymbol(arr.get(0));
            rule.setFrom(noTermSymbol);
            rule.setIn(arr.get(1));
            ruleList.add(rule);
        }
    }


    private static void initTestDataForChainRule() {
        n = 3;
        k = 2;
        l = 12;
        noTermSymbolList.add(new NoTermSymbol("A"));
        noTermSymbolList.add(new NoTermSymbol("B"));
        noTermSymbolList.add(new NoTermSymbol("C"));
        startSymbol = new NoTermSymbol("S");

        for (char c = 'a'; c < 'c'; c++) {
            alphabetList.add(String.valueOf(c));
        }

        ruleList.add(new Rule(startSymbol, "A"));
        ruleList.add(new Rule(startSymbol, "bC"));
        ruleList.add(new Rule(startSymbol, "C"));
        ruleList.add(new Rule(noTermSymbolList.get(0), "bA"));
        ruleList.add(new Rule(noTermSymbolList.get(0), "B"));
        ruleList.add(new Rule(noTermSymbolList.get(0), "BA"));
        ruleList.add(new Rule(noTermSymbolList.get(1), "a"));
        ruleList.add(new Rule(noTermSymbolList.get(1), "C"));
        ruleList.add(new Rule(noTermSymbolList.get(1), "CS"));
        ruleList.add(new Rule(noTermSymbolList.get(2), "b"));
        ruleList.add(new Rule(noTermSymbolList.get(2), "aba"));
        ruleList.add(new Rule(noTermSymbolList.get(2), "aS"));
    }

    private static void initTestDataForERule() {
        n = 2;
        k = 4;
        l = 7;
        noTermSymbolList.add(new NoTermSymbol("A"));
        noTermSymbolList.add(new NoTermSymbol("B"));
        startSymbol = new NoTermSymbol("S");

        for (char c = 'a'; c < 'e'; c++) {
            alphabetList.add(String.valueOf(c));
        }

        ruleList.add(new Rule(startSymbol, "AAaB"));
        ruleList.add(new Rule(startSymbol, "bB"));
        ruleList.add(new Rule(noTermSymbolList.get(0), "cAdA"));
        ruleList.add(new Rule(noTermSymbolList.get(0), "a"));
        ruleList.add(new Rule(noTermSymbolList.get(0), "#"));
        ruleList.add(new Rule(noTermSymbolList.get(1), "cBdd"));
        ruleList.add(new Rule(noTermSymbolList.get(1), "#"));
    }
}
