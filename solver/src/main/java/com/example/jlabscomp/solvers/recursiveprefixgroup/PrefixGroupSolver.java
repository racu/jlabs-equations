package com.example.jlabscomp.solvers.recursiveprefixgroup;

import com.example.jlabscomp.solvers.recursiveprefixgroup.cache.MultiValueKey;
import com.example.jlabscomp.solvers.recursiveprefixgroup.cache.PartialSolutionCache;
import com.example.jlabscomp.solvers.parser.EquationParser;
import com.example.jlabscomp.solvers.parser.ParsedEquation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class PrefixGroupSolver {

    @Autowired
    private EquationParser parser;

    PartialSolutionCache cache = new PartialSolutionCache();

    public String[] solve(String[] equations){
        String[] answers = new String[equations.length];
        for(int eqI = 0; eqI < equations.length; eqI++) {
            ParsedEquation eq = parser.parse(equations[eqI]);
            LinkedList<Integer> valuesLinkedList = new LinkedList<Integer>(Arrays.asList(eq.getValues()));
            String ans = solveSingle(valuesLinkedList, eq.getRes());
            answers[eqI] = ans;
        }
        return answers;
    }

    public String[] solve(List<ParsedEquation> equations){
        String[] answers = new String[equations.size()];
        for(int eqI = 0; eqI < equations.size(); eqI++) {
            ParsedEquation eq = equations.get(eqI);
            LinkedList<Integer> valuesLinkedList = new LinkedList<Integer>(Arrays.asList(eq.getValues()));
            String ans = solveSingle(valuesLinkedList, eq.getRes());
            answers[eqI] = ans;
        }
        return answers;
    }

    private String solveSingle(LinkedList<Integer> values, long res) {
        long groupValue = 1;

        //check cache how to achieve
        if(values.size() == 3) {
            Iterator<Integer> iterator = values.iterator();
            String cachedSolution = cache.getCachedSolution(new MultiValueKey(iterator.next(), iterator.next(), iterator.next()), res);
            if("IMP".equals(cachedSolution))
                return null;
            if(cachedSolution != null)
                return cachedSolution;
        }

        LinkedList<Integer> valuesCopy = (LinkedList<Integer>)values.clone();


        //group empty, expected 0 - success
        if(valuesCopy.size() == 0 && res == 0)
            return "";

        for(int groupSize = 1; groupSize <= values.size(); groupSize++){
            groupValue *= valuesCopy.pollFirst();

            //only multiplications and equal to expected result
            if(valuesCopy.size() == 0 && res == groupValue) {
                String operators = getMultText(groupSize - 1);
                if(values.size() == 3) {
                    Iterator<Integer> iterator = values.iterator();
                    cache.storeInCache(new MultiValueKey(iterator.next(), iterator.next(), iterator.next()), res, operators);
                }
                return operators;
            }

            // + after group
            String sol;
            sol = solveSingle(valuesCopy, res - groupValue);
            if(sol != null) {
                String operators = getMultText(groupSize - 1) + "+" + sol;
                if(values.size() == 3) {
                    Iterator<Integer> iterator = values.iterator();
                    cache.storeInCache(new MultiValueKey(iterator.next(), iterator.next(), iterator.next()), res, operators);
                }
                return operators;
            }

            // - after group
            invertFirstElem(valuesCopy);
            sol = solveSingle(valuesCopy, res - groupValue );
            invertFirstElem(valuesCopy);
            if(sol != null) {
                String operators = getMultText(groupSize - 1) + "-" + sol;
                if(values.size() == 3) {
                    Iterator<Integer> iterator = values.iterator();
                    cache.storeInCache(new MultiValueKey(iterator.next(), iterator.next(), iterator.next()), res, operators);
                }
                return operators;
            }

        }
        if(values.size() == 3) {
            Iterator<Integer> iterator = values.iterator();
            cache.storeInCacheEmptyResult(new MultiValueKey(iterator.next(), iterator.next(), iterator.next()));
        }
        return null;
    }

    private void invertFirstElem(LinkedList<Integer> values){
        if(values.size() > 0) {
            Integer firstVal = values.pollFirst();
            values.addFirst(-firstVal);
        }
    }

    private String getMultText(int count){
        return String.join("", Collections.nCopies(count, "*"));
    }


}
