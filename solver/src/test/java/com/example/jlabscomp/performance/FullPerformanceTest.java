package com.example.jlabscomp.performance;

import com.example.jlabscomp.EquationsUtils;
import com.example.jlabscomp.jlabsgateway.LocalServerGateway;
import com.example.jlabscomp.solvers.memoizing.MemoizingSolver;
import com.example.jlabscomp.solvers.parser.FastEquationParser;
import com.example.jlabscomp.solvers.parser.ParsedEquation;
import com.example.jlabscomp.verifier.SolutionVerifier;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
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
    FastEquationParser parser;

    @Autowired
    EquationsUtils utils;

    @Autowired
    MemoizingSolver solver;


    @Test
    @SneakyThrows
    public void testPerformanceWithLocalServer(){
        List<Long> retrievingTimes = new ArrayList<>();
        List<Long> parsingTimes = new ArrayList<>();
        List<Long> solutionTimes = new ArrayList<>();
        List<Long> convertionTimes = new ArrayList<>();
        List<Long> submittedTimes = new ArrayList<>();


        for(int testCaseInd=0; testCaseInd < 100; testCaseInd++) {

            //retrieve equations
            long ms = System.currentTimeMillis();
            String[] equations =  localServer.retrieveTestCase().equations;
            ms = (System.currentTimeMillis() - ms);
            System.out.println("retrieving: " + ms + " ms");
            if(testCaseInd>0)
                retrievingTimes.add(ms);

            //parse
            ms = System.currentTimeMillis();
            List<ParsedEquation> parsedEquations = parser.parse(equations);
            ms = (System.currentTimeMillis() - ms);
            System.out.println("parsing: " + ms + " ms");
            if(testCaseInd>0)
                parsingTimes.add(ms);

            //solving time
            ms = System.currentTimeMillis();
            List<String[]> answers = solver.solve(parsedEquations);
            ms = (System.currentTimeMillis() - ms);
            System.out.println("solved: " + ms + " ms");
            if(testCaseInd>0)
                solutionTimes.add(ms);

            //convertion
            ms = System.currentTimeMillis();
            //String s = utils.convertResultsToSubmittableOutputString(answers);
            ms = (System.currentTimeMillis() - ms);
            System.out.println("converted: " + ms + " ms");
            if(testCaseInd>0)
                convertionTimes.add(ms);


            //submit answers
            ms = System.currentTimeMillis();
            localServer.submitTestCaseAnswers(answers);
           // localServer.submitTestCaseAnswers(s);
            ms = (System.currentTimeMillis() - ms);
            System.out.println("submitted: " + ms + " ms");
            if(testCaseInd>0)
                submittedTimes.add(ms);

            //verifier.verify(answersToSubmit, parser.parse(equations));
            //verifier.verify(answers, parser.parse(equations));

        }

        System.out.println();
        System.out.println("retrieving times p80: " + percentile(retrievingTimes, 80));
        System.out.println("parsing times p80: " + percentile(parsingTimes, 80));
        System.out.println("solution times p80: " + percentile(solutionTimes, 80));
        System.out.println("converted times p80: " + percentile(convertionTimes, 80));
        System.out.println("submitted times p80: " + percentile(submittedTimes, 80));
        //System.out.println("convertion times p80: " + percentile(convertionTimes, 80));

//        retrieving times p80: 25
//        parsing times p80: 14
//        solution times p80: 46
//        submitted times p80: 30
    }




    public static long percentile(List<Long> times, double Percentile)
    {
        times.sort(Long::compare);
        int Index = (int)Math.ceil(((double)Percentile / (double)100) * (double)times.size());
        return times.get(Index-1);
    }






}
