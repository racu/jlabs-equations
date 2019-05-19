package com.example.jlabscomp.solvers;

import com.example.jlabscomp.solvers.parser.EquationParser;
import com.example.jlabscomp.solvers.parser.ParsedEquation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Component
public class SimpleSolver {

    @Autowired
    EquationParser parser;



    public String[] solve(String[] equations){

        String[] answers = new String[equations.length];
        for(int eqI = 0; eqI < equations.length; eqI++) {
            ParsedEquation eq = parser.parse(equations[eqI]);
            String ans = solveSingle(eq.getValues(), eq.getRes());
            answers[eqI] = ans;

        }
        return answers;
    }

    private String solveSingle(Integer[] values, long expectedResult) {

        char[] ops = {'+', '-', '*'};
        try {
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            for (char op1 : ops) {
                String expr1 = String.valueOf(values[0]) + op1 + String.valueOf(values[1]);
                for (char op2 : ops) {
                    String expr2 = expr1 + op2 + String.valueOf(values[2]);
                    Object result = engine.eval(expr2);
                    int calcResult = ((Integer) result).intValue();
                    if (calcResult == expectedResult) {
                        return (String.format("[\"%s\",\"%s\"]", op1, op2));
                    }

                }
            }
        }
        catch(Exception ex){
            throw new RuntimeException(ex);
        }
        throw new RuntimeException("solution not found!");
    }


}
