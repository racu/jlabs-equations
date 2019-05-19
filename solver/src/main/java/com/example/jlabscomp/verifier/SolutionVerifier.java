package com.example.jlabscomp.verifier;

import com.example.jlabscomp.solvers.parser.ParsedEquation;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.List;

@Component
public class SolutionVerifier {

    @SneakyThrows
    public void verify(List<String[]> solutions, List<ParsedEquation> equations){

        for(int eqI=0; eqI < equations.size(); eqI++){
            ParsedEquation eq = equations.get(eqI);
            String[] sol = solutions.get(eqI);

            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");

            StringBuilder exp = new StringBuilder();
            int variablesCount = eq.getValues().length;
            for(int varI=0; varI < variablesCount - 1; varI++)
                exp.append(eq.getValues()[varI] + " " + sol[varI]);
            exp.append(eq.getValues()[variablesCount - 1]);

            Object result = engine.eval(exp.toString());
            long calcResult = 0;
            if(result instanceof Integer)
                 calcResult = ((Integer) result).intValue();
            else
                 calcResult = (long) Math.round( ((Double) result).doubleValue());
            if (calcResult != eq.getRes()) {
                throw new RuntimeException("Invalid solution "+eqI);
            }
        }

    }
}
