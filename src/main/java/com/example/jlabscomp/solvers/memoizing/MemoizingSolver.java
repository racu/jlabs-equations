package com.example.jlabscomp.solvers.memoizing;

import com.example.jlabscomp.solvers.parser.EquationParser;
import com.example.jlabscomp.solvers.parser.ParsedEquation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MemoizingSolver {

    @Data
    @AllArgsConstructor
    public static class Entry{
        public long result;
        public long group;
    }

    @Autowired
    private EquationParser parser;


    public String[] solve(List<ParsedEquation> equations){
        String[] answers = new String[equations.size()];
        for(int eqI = 0; eqI < equations.size(); eqI++) {
            ParsedEquation eq = equations.get(eqI);
            //LinkedList<Integer> valuesLinkedList = new LinkedList<Integer>(Arrays.asList(eq.getValues()));
            String ans = solveSingle(eq.getValues(), eq.getRes());
            answers[eqI] = ans;
        }
        return answers;
    }


    private String solveSingle(Integer[] values, long res) {

        List<Entry>[] positionResultLastGroup = new List[values.length];
        for(int i=0; i<values.length; i++)
            positionResultLastGroup[i] = new ArrayList<>(100);

        //initial value - last group and result equal first element
        positionResultLastGroup[0].add(new Entry(values[0], values[0]));

        long result,group;
        int nextStep;
        for(int step = 0; step < values.length - 1; step++){
            nextStep = step + 1;
            List<Entry> entries = positionResultLastGroup[nextStep];
            long nextValue = values[nextStep];

            for(Entry entry : positionResultLastGroup[step]){
                result = entry.result;
                group = entry.group;

                //+
                //storeResult(positionResultLastGroup[step + 1],  result + values[step + 1],  values[step + 1]);
                entries.add(new Entry(result + nextValue, nextValue));

                //-
                //storeResult(positionResultLastGroup[step + 1],  result - values[step + 1],  - values[step + 1]);
                entries.add(new Entry(result - nextValue, - nextValue));

                //*
                //storeResult(positionResultLastGroup[step + 1],  result - group + group * values[step + 1],  group * values[step+1]);
                entries.add(new Entry(result - group + group * nextValue, group * nextValue));
            }
        }

//        if(positionResultLastGroup[values.length - 1].get(res).keySet().size() == 0)
//            throw new RuntimeException();
        return "";

    }


    private void storeResult(HashMap<Long, Map<Long, Boolean>> resultLastGroup, long result, long group){
        Map<Long, Boolean> groupMapForResult = resultLastGroup.get(result);
        if(groupMapForResult == null){
            groupMapForResult = new HashMap<>(1);
            resultLastGroup.put(result, groupMapForResult);
        }
        groupMapForResult.put(group, Boolean.TRUE);
    }


}
