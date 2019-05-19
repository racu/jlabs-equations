package com.example.jlabscomp.solvers.recursiveprefixgroup.cache;

import java.util.HashMap;
import java.util.Map;

public class PartialSolutionCache {

    public static boolean FIRST_ROUND = true;

    Map<MultiValueKey, Map<Long,String>> valuesCombinationsMap = new HashMap<>();

    public String getCachedSolution(int v1, int v2, int v3, long result){
        MultiValueKey multiValueKey = new MultiValueKey(v1, v2, v3);
        return getCachedSolution(multiValueKey, result);
    }

    public String getCachedSolution(MultiValueKey multiValueKey, long result){
        Map<Long, String> possibleResultsMap = valuesCombinationsMap.get(multiValueKey);
        if(possibleResultsMap == null)
            return null;
        String operators = possibleResultsMap.get(result);
        if(!FIRST_ROUND && operators == null)
            return "IMP";
        return operators;
    }

    public void storeInCache(int v1, int v2, int v3, long result, String operators){
        MultiValueKey multiValueKey = new MultiValueKey(v1, v2, v3);
        storeInCache(multiValueKey, result, operators);
    }
    public void storeInCache(MultiValueKey multiValueKey, long result, String operators){
        Map<Long, String> resultToOperatorsMap = valuesCombinationsMap.get(multiValueKey);
        if(resultToOperatorsMap == null){
            resultToOperatorsMap =  new HashMap<Long, String>();
            valuesCombinationsMap.put(multiValueKey, resultToOperatorsMap);
        }
        resultToOperatorsMap.put(result, operators);
    }

    public void storeInCacheEmptyResult(MultiValueKey multiValueKey ){
        Map<Long, String> resultToOperatorsMap = valuesCombinationsMap.get(multiValueKey);
        if(resultToOperatorsMap == null){
            resultToOperatorsMap =  new HashMap<Long, String>();
            valuesCombinationsMap.put(multiValueKey, resultToOperatorsMap);
        }
    }

}
