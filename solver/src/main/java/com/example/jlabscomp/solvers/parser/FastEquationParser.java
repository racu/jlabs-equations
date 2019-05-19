package com.example.jlabscomp.solvers.parser;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

@Component
public class FastEquationParser {


//    public ParsedEquation parse(String eqText){
//        List<Long> numbers = new ArrayList<>();
//        StringTokenizer tok = new StringTokenizer(eqText," ?=");
//        while(tok.hasMoreTokens())
//            numbers.add(Long.parseLong(tok.nextToken()));
//        Integer[] values = new Integer[numbers.size() - 1];
//        for(int valInd = 0; valInd < numbers.size() - 1; valInd ++)
//            values[valInd] = numbers.get(valInd).intValue();
//        long res = numbers.get(numbers.size() - 1);
//        return new ParsedEquation(values, res);
//    }

    public ParsedEquation parse(String eqText){
        int pos = eqText.length() - 1;
        do{
            pos--;
        }while(eqText.charAt(pos) != ' ');

        //read res
        long res = Long.parseLong(eqText.substring(pos+1));
        //set the end of values
        int terminatingPos = pos - 2;


        int startPos = 0;
        pos = 0;

        List<Integer> values = new ArrayList<>(6);
        while(pos < terminatingPos) {
            while (pos < terminatingPos && eqText.charAt(pos) != ' ')
                pos++;
            values.add(Integer.parseInt(eqText.substring(startPos, pos)));
            startPos = pos + 3;
            pos = startPos;
        }

        return new ParsedEquation(values.toArray(new Integer[0]), res);
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
