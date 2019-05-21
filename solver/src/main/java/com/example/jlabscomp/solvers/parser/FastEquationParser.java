package com.example.jlabscomp.solvers.parser;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

@Component
public class FastEquationParser {


    //retrieve number without creating substring
    public static int getPositiveNumber(String data, int start, int endExc){
        int number = 0;
        for (int i = start; i <endExc; i++)
           number = (number * 10) + (data.charAt(i) - 48);
        return number;
    }

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
            values.add(getPositiveNumber(eqText, startPos, pos));
            startPos = pos + 3;
            pos = startPos;
        }

        return new ParsedEquation(values.toArray(new Integer[0]), res);
    }

    public List<ParsedEquation> parse(List<String> equations){
        List<ParsedEquation> parsedEquations = new ArrayList<>();
        int eqI = 0;
        try {
            for (eqI = 0; eqI < equations.size(); eqI++) {
                ParsedEquation eq = parse(equations.get(eqI));
                parsedEquations.add(eq);
            }
            return parsedEquations;
        }catch(Exception ex){
            System.out.println("error in parsing equation "+eqI);
            throw new RuntimeException("Error in parsing");
        }
    }

    public List<ParsedEquation> parse(String[] equations){
        List<ParsedEquation> parsedEquations = new ArrayList<>();
        int eqI = 0;
        try {
            for (eqI = 0; eqI < equations.length; eqI++) {
                ParsedEquation eq = parse(equations[eqI]);
                parsedEquations.add(eq);
            }
            return parsedEquations;
        }catch(Exception ex){
            System.out.println("error in parsing equation "+eqI);
            throw new RuntimeException("Error in parsing");
        }
    }



}
