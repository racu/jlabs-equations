package com.example.jlabscomp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiagnosticMetrics {

    Map<String, List<Long>> metrics = new HashMap<>(10);

    public long startTimer(){
        return System.currentTimeMillis();
    }

    public long saveElapsedForMetric(String metricName, long startTime){
        long measuredTime = System.currentTimeMillis() - startTime;
        List<Long> recordedValues = metrics.get(metricName);
        if(recordedValues == null){
            recordedValues = new ArrayList<>(10);
            metrics.put(metricName, recordedValues);
        }
        recordedValues.add(measuredTime);
        return measuredTime;
    }


    public void printPercentiles(int percentile){
        for(String metricName : metrics.keySet())
            printPercentiles(metricName,  percentile);
    }

    public void printPercentiles(String metricName, int percentile){
        List<Long> values = metrics.get(metricName);
        ArrayList<Long> clonedLongs = new ArrayList<>(values);
        System.out.println(metricName+": "+percentile(clonedLongs, percentile)+" ms");
    }

    public long percentile(List<Long> times, double Percentile) {
        times.sort(Long::compare);
        int Index = (int)Math.ceil(((double)Percentile / (double)100) * (double)times.size());
        int targetIndex = Index-1;
        if(targetIndex < 0)
            targetIndex = 0;
        if(targetIndex >= times.size())
            targetIndex = times.size() -1;
        return times.get(targetIndex);
    }
}
