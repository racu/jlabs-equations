package com.example.jlabscomp.equationsprocessor;

import com.example.jlabscomp.DiagnosticMetrics;
import com.example.jlabscomp.EquationsUtils;
import com.example.jlabscomp.solvers.memoizing.MemoizingSolver;
import com.example.jlabscomp.solvers.parser.FastEquationParser;
import com.example.jlabscomp.solvers.parser.ParsedEquation;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Future;

//from input to end output
@Component
public class DefaultEquationsProcessor {

//    @Autowired
//    SolutionVerifier verifier;

    @Autowired
    FastEquationParser parser;

    @Autowired
    MemoizingSolver solver;

    @Autowired
    EquationsUtils utils;

    ThreadPoolTaskExecutor executor;


    @PostConstruct
    public void init(){
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("default_task_executor_thread");
        executor.initialize();
    }


    //--------------------------------------------MULTI THREADED TO LIST - BEST! --------------------------------------------------

    @SneakyThrows
    public List<String[]> solveToListConcurrent(String[] equations, DiagnosticMetrics metrics){

        long startTime;

        startTime = metrics.startTimer();
        //pick number of threads
        int threadsCount = evaluateNumberOfThreads(equations.length);
        //partition equations for multithreading
        List<List<String>> partitionedEquations = splitTasksForThreads(threadsCount, Arrays.asList(equations));
        metrics.saveElapsedForMetric("partitioning", startTime);

        //parse and solve
        startTime = metrics.startTimer();
        List<String[]> answers = processWithThreadsToAnswersList(equations, threadsCount, partitionedEquations);
        metrics.saveElapsedForMetric("parseAndSolve", startTime);
        return answers;
    }

    private List<String[]> processWithThreadsToAnswersList(String[] equations, int threadsCount, List<List<String>> partitionedEquations) throws InterruptedException, java.util.concurrent.ExecutionException {
        List<String[]> answers = new ArrayList<>(equations.length);
        Future[] futures = new Future[threadsCount];
        for(int i=0; i<threadsCount; i++) {
            List<String> equationsPart = partitionedEquations.get(i);
            futures[i] = executor.submit( () -> {
                List<ParsedEquation> parsedEquations = parser.parse(equationsPart);
                List<String[]> answersPart = solver.solve(parsedEquations);
                return answersPart;
            });
        }
        for(int i=0; i<threadsCount; i++)
            answers.addAll((List<String[]>)(futures[i].get()));
        return answers;
    }



    //--------------------------------------------MULTI THREADED TO TEXT --------------------------------------------------
    @SneakyThrows
    public String solveToTextConcurrent(String[] equations, DiagnosticMetrics metrics){

        long startTime;

        startTime = metrics.startTimer();
        //pick number of threads
        int threadsCount = evaluateNumberOfThreads(equations.length);
        //partition equations for multithreading
        List<List<String>> partitionedEquations = splitTasksForThreads(threadsCount, Arrays.asList(equations));
        metrics.saveElapsedForMetric("partitioning", startTime);

        //parse and solve
        startTime = metrics.startTimer();
        String answers = processWithThreadsToAnswerText(threadsCount, partitionedEquations);
        metrics.saveElapsedForMetric("parseAndSolve", startTime);
        return answers;
    }

    private String processWithThreadsToAnswerText(int threadsCount, List<List<String>> partitionedEquations) throws InterruptedException, java.util.concurrent.ExecutionException {
        Future[] futures = new Future[threadsCount];
        for(int i=0; i<threadsCount; i++) {
            List<String> equationsPart = partitionedEquations.get(i);
            futures[i] = executor.submit( () -> {
                List<ParsedEquation> parsedEquations = parser.parse(equationsPart);
                List<String[]> answersPart = solver.solve(parsedEquations);
                String answerJson = utils.convertListResultsToSubmittableOutputString(answersPart);
                return answerJson;
            });
        }
        StringJoiner stringJoiner = new StringJoiner(",", "[", "]");
        for(int i=0; i<threadsCount; i++)
            stringJoiner.add((String)(futures[i].get()));
        return stringJoiner.toString();
    }





    public int evaluateNumberOfThreads(int equationsCount) {
        if(equationsCount > 20)
            return 4;
        return 1;
    }

    public List<List<String>> splitTasksForThreads(int threadsCount, List<String> equations){
        int chunkSize = equations.size() / threadsCount + 1;
        List<List<String>> partitionedEquations = Lists.partition(equations, chunkSize);
        return partitionedEquations;
    }





    //---------------------------------------------SINGLE THREADED ------------------------------------------

    public List<String[]> solveToList(String[] equations, DiagnosticMetrics metrics){

        long startTime;

        //parse
        startTime = metrics.startTimer();
        List<ParsedEquation> parsedEquations = parser.parse(equations);
        metrics.saveElapsedForMetric("parsing", startTime);

        //solve
        startTime = metrics.startTimer();
        List<String[]> answers = solver.solve(parsedEquations);
        metrics.saveElapsedForMetric("solving", startTime);

        //verifier.verify(answersToSubmit, parser.parse(equations));
        //verifier.verify(answers, parser.parse(equations));
        return answers;
    }

    public List<String[]> solveToString(String[] equations, DiagnosticMetrics metrics){

        long startTime;

        //parse and solve
        List<String[]> answers = solveToList(equations, metrics);

        //convertion
        startTime = metrics.startTimer();
        String s = utils.convertListResultsToSubmittableOutputString(answers);
        metrics.saveElapsedForMetric("converting", startTime);

        //verifier.verify(answersToSubmit, parser.parse(equations));
        //verifier.verify(answers, parser.parse(equations));
        return answers;
    }


    public void shutdown() {
        executor.shutdown();
    }
}
