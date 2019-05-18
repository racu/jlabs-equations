package com.example.jlabscomp.solvers.parser;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

@Component
public class FastEquationParser {
    public ParsedEquation parse(String eqText){
        List<Long> numbers = new ArrayList<>();
        StringTokenizer tok = new StringTokenizer(eqText);
        numbers.add(Long.parseLong(tok.nextToken()));
        while(tok.hasMoreTokens()){
            tok.nextToken();
            numbers.add(Long.parseLong(tok.nextToken()));
        }
        Integer[] values = new Integer[numbers.size() - 1];
        for(int valInd = 0; valInd < numbers.size() - 1; valInd ++)
            values[valInd] = numbers.get(valInd).intValue();
        long res = numbers.get(numbers.size() - 1);
        return new ParsedEquation(values, res);

    }

    public List<ParsedEquation> parse(String[] equations){
        List<ParsedEquation> parsedEquations = new ArrayList<>();
        for(int eqI = 0; eqI < equations.length; eqI++) {
            ParsedEquation eq = parse(equations[eqI]);
            parsedEquations.add(eq);
        }
        return parsedEquations;
    }
}
