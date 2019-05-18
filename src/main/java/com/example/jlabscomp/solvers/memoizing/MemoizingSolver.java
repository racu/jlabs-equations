package com.example.jlabscomp.solvers.memoizing;

import com.example.jlabscomp.solvers.parser.EquationParser;
import com.example.jlabscomp.solvers.parser.ParsedEquation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MemoizingSolver {

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

        HashMap<Long, Map<Long, Boolean>>[] positionResultLastGroup = new HashMap[values.length];
        for(int i=0; i<values.length; i++)
            positionResultLastGroup[i] = new HashMap<>();

        //initial value - last group and result equal first element
        Map<Long,Boolean> groupsMap = new HashMap<>();
        groupsMap.put(values[0].longValue(), true);
        positionResultLastGroup[0].put(values[0].longValue(), groupsMap);

        for(int step = 0; step < values.length - 1; step++){
            for(long result : positionResultLastGroup[step].keySet()){
                for(long group : positionResultLastGroup[step].get(result).keySet()){

                    //+
                    storeResult(positionResultLastGroup[step + 1],  result + values[step + 1],  values[step + 1]);

                    //-
                    storeResult(positionResultLastGroup[step + 1],  result - values[step + 1],  - values[step + 1]);

                    //*
                    storeResult(positionResultLastGroup[step + 1],  result - group + group * values[step + 1],  group * values[step+1]);

                }
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
        groupMapForResult.put(group, true);
    }


}
