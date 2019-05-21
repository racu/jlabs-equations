package com.example.jlabscomp.performance;

import com.example.jlabscomp.equationsprocessor.DefaultEquationsProcessor;
import com.example.jlabscomp.DiagnosticMetrics;
import com.example.jlabscomp.EquationsUtils;
import com.example.jlabscomp.jlabsgateway.LocalServerGateway;
import com.example.jlabscomp.solvers.memoizing.MemoizingSolver;
import com.example.jlabscomp.solvers.parser.FastEquationParser;
import com.example.jlabscomp.verifier.SolutionVerifier;
import lombok.SneakyThrows;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FullPerformanceTest {

    @Autowired
    LocalServerGateway localServer;

    @Autowired
    SolutionVerifier verifier;

    @Autowired
    DefaultEquationsProcessor eqProc;

    @Autowired
    FastEquationParser parser;


    @Test
    @Ignore
    @SneakyThrows
    public void testPerformanceWithLocalServer(){

        DiagnosticMetrics metrics = new DiagnosticMetrics();

        for(int testCaseInd=0; testCaseInd < 100; testCaseInd++) {

            long msTotal =  metrics.startTimer();

            //retrieve equations
            long ms = metrics.startTimer();
            String[] equations =  localServer.retrieveTestCase().equations;
            ms = metrics.saveElapsedForMetric("retrieving", ms);
            System.out.println("retrieving: " + ms + " ms");

            //solve
            List<String[]> answers = eqProc.solveToListConcurrent(equations, metrics);
            //String answers = eqProc.solveToTextConcurrent(equations, metrics);
            //List<String[]> answers = eqProc.solveToList(equations, metrics);

            //submit answers
            ms = metrics.startTimer();
            localServer.submitTestCaseAnswers(answers);  //submit list
            // localServer.submitTestCaseAnswers(s);     //submit string
            ms = metrics.saveElapsedForMetric("submitted", ms);
            System.out.println("submitted: " + ms + " ms");


            msTotal = metrics.saveElapsedForMetric("total", msTotal);
            System.out.println("total: " + msTotal + " ms");

            //verifier.verify(answersToSubmit, parser.parse(equations));
            //verifier.verify(answers, parser.parse(equations));

        }


        System.out.println();
        metrics.printPercentiles(80);

//        SINGLE THREADED TO LIST:
//        total: 381
//        retrieving: 73
//        parsing: 48
//        solving: 174
//        submitted: 95

//        MULTI THREADED TO TEXT:


//        MULTI THREADED TO LIST:
//        total: 273
//        retrieving: 72
//        partitioning: 0
//        parseAndSolve: 106
//        submitted: 99
    }




    public static long percentile(List<Long> times, double Percentile)
    {
        times.sort(Long::compare);
        int Index = (int)Math.ceil(((double)Percentile / (double)100) * (double)times.size());
        return times.get(Index-1);
    }






}
