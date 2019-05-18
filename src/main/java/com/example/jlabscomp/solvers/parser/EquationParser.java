package com.example.jlabscomp.solvers.parser;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EquationParser {
    public ParsedEquation parse(String eqText){
        String[] split = eqText.split(" ", -1);

        int countValues = split.length / 2;
        Integer[] values = new Integer[countValues];
        for(int valInd = 0; valInd < countValues; valInd ++) {
            values[valInd] = Integer.parseInt(split[2 * valInd]);
        }
        long res = Long.parseLong(split[split.length - 1]);
        return new ParsedEquation(values, res);
    }

    //GUAVA
//    public ParsedEquation parseGuava(String eqText){
//        //String[] split = eqText.split(" ", -1);
//        List<String> split = Splitter.on(' ').splitToList(eqText);
//
//        int countValues = split.size() / 2;
//        Integer[] values = new Integer[countValues];
//        for(int valInd = 0; valInd < countValues; valInd ++) {
//            values[valInd] = Integer.parseInt(split.get(2 * valInd));
//        }
//        long res = Long.parseLong(split.get(split.size() - 1));
//        return new ParsedEquation(values, res);
//    }


    public List<ParsedEquation> parse(String[] equations){
        List<ParsedEquation> parsedEquations = new ArrayList<>();
        for(int eqI = 0; eqI < equations.length; eqI++) {
            ParsedEquation eq = parse(equations[eqI]);
            parsedEquations.add(eq);
        }
        return parsedEquations;
    }
}
