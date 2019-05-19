package com.example.jlabscomp.solvers.memoizing;

import com.example.jlabscomp.solvers.parser.ParsedEquation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MemoizingSolver {

    @AllArgsConstructor
    public static class ResultEntry {
        public long result;
        public long group;
        public int prevIndex;
        public String operator;
    }


    public List<String[]> solve(List<ParsedEquation> equations){
        List<String[]> answers = new ArrayList(equations.size());
        for(int eqI = 0; eqI < equations.size(); eqI++) {
            ParsedEquation eq = equations.get(eqI);
            String[] ans = solveSingle(eq.getValues(), eq.getRes());
            answers.add(ans);
        }
        return answers;
    }


    private String[] solveSingle(Integer[] values, long res) {

        List<ResultEntry>[] positionResultLastGroup = new List[values.length];

        //prepare sizes exactly as are required, corresponding to branching factor 3
        int multiplier = 1;
        for(int i=0; i < values.length; i++) {
            positionResultLastGroup[i] = new ArrayList<>(multiplier); //pre-initialize
            multiplier *= 3;
        }

        //initial value - last group and result equal first element
        positionResultLastGroup[0].add(new ResultEntry(values[0], values[0], -1, ""));

        long result, group;
        int nextStep, nextValue;
        ResultEntry entry;

        for(int step = 0; step < values.length - 1; step++){
            nextStep = step + 1;
            List<ResultEntry> results = positionResultLastGroup[step];
            List<ResultEntry> nextResults = positionResultLastGroup[nextStep];
            nextValue = values[nextStep];

            for(int entryInd=0; entryInd < results.size() ; entryInd++){
                entry = results.get(entryInd);
                result = entry.result;
                group = entry.group;
                //+
                nextResults.add(new ResultEntry(result + nextValue, nextValue, entryInd,"+"));
                //-
                nextResults.add(new ResultEntry(result - nextValue, - nextValue, entryInd,"-"));
                //*
                nextResults.add(new ResultEntry(result - group + group * nextValue, group * nextValue, entryInd,"*"));
            }
        }

        //find entry giving correct result in last step
        List<ResultEntry> results = positionResultLastGroup[values.length - 1];
        int entryInd;
        for(entryInd=0; entryInd < results.size() ; entryInd++)
            if(results.get(entryInd).result == res)
                break;
        String[] answer = new String[values.length - 1];
        for(int step = values.length - 1; step > 0 ; step--){
            ResultEntry resultEntry = positionResultLastGroup[step].get(entryInd);
            answer[step - 1] = resultEntry.operator;
            entryInd = resultEntry.prevIndex;
        }
        return answer;
    }

}
