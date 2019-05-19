package com.example.jlabscomp.performance;

import com.example.jlabscomp.EquationsUtils;
import com.example.jlabscomp.solvers.memoizing.MemoizingSolver;
import com.example.jlabscomp.solvers.recursiveprefixgroup.PrefixGroupSolver;
import com.example.jlabscomp.solvers.recursiveprefixgroup.cache.PartialSolutionCache;
import com.example.jlabscomp.solvers.parser.EquationParser;
import com.example.jlabscomp.solvers.parser.FastEquationParser;
import com.example.jlabscomp.solvers.parser.ParsedEquation;
import com.example.jlabscomp.storage.LocalTestCasesStorage;
import com.example.jlabscomp.verifier.SolutionVerifier;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PerformanceTest {

    String performanceStorePath = "E:\\utils\\playground\\algos\\jlabsedovx2\\cases\\performance1\\case_1353454811441596726";

    @Autowired
    LocalTestCasesStorage store;

    @Autowired
    MemoizingSolver solver;

    @Autowired
    SolutionVerifier verifier;

    @Autowired
    EquationParser parser;

    @Autowired
    FastEquationParser parser2;

    @Autowired
    EquationsUtils utils;



    @Test
    public void testPerformance(){
        List<Long> parsingTimes = new ArrayList<>();
        List<Long> solutionTimes = new ArrayList<>();
        List<Long> convertionTimes = new ArrayList<>();

        String[] equations = store.load(performanceStorePath);
        for(int testCaseInd=0; testCaseInd < 100; testCaseInd++) {

            long ms = System.currentTimeMillis();
            List<ParsedEquation> parsedEquations = parser.parse(equations);
            ms = (System.currentTimeMillis() - ms);
            System.out.println("parsing: " + ms + " ms");
            if(testCaseInd>0)
                parsingTimes.add(ms);

            //solving time
            ms = System.currentTimeMillis();
           // String[] answers = solver.solve(parsedEquations);
            List<String[]> answers = solver.solve(parsedEquations);
            ms = (System.currentTimeMillis() - ms);
            System.out.println("solved: " + ms + " ms");
            if(testCaseInd>0)
                solutionTimes.add(ms);

            //convertion time
            ms = System.currentTimeMillis();
//            List<String[]> answersToSubmit = utils.convertResultsToSubmittableOutput(answers);
            String answersToSubmit = utils.convertResultsToSubmittableOutputString(answers);
            ms = (System.currentTimeMillis() - ms);
            System.out.println("converted: " + ms + " ms");
            if(testCaseInd>0)
                convertionTimes.add(ms);

            //verifier.verify(answersToSubmit, parser.parse(equations));
            //verifier.verify(answers, parser.parse(equations));
        }

        System.out.println("parsing times p80: " + percentile(parsingTimes, 80));
        System.out.println("solution times p80: " + percentile(solutionTimes, 80));
        System.out.println("convertion times p80: " + percentile(convertionTimes, 80));
        //parsing 24
        //solutiontimes p95: 145
        //convertionTimes p95: 13
    }


    @Test
    public void testPerformanceConvertOutput(){
        List<String> results = new ArrayList<>(100000);
        for(int i=0;i<100000;i++)
            results.add("ABCDE");

        for(int i=0;i<10;i++) {
            long ms = System.currentTimeMillis();
            utils.convertResultsToSubmittableOutputString(results.toArray(new String[0]));
            System.out.println("converted to string: " + (System.currentTimeMillis() - ms) + " ms");

            ms = System.currentTimeMillis();
            utils.convertResultsToSubmittableOutput(results.toArray(new String[0]));
            System.out.println("converted to array: " + (System.currentTimeMillis() - ms) + " ms");
        }
    }


    @Test
    public void testPerformanceParsingInput() {
        List<Long> parsingTimes = new ArrayList<>();
        List<Long> parsingTimes2 = new ArrayList<>();

        String[] equations = store.load(performanceStorePath);
        for (int testCaseInd = 0; testCaseInd < 100; testCaseInd++) {
            long ms = System.currentTimeMillis();
            List<ParsedEquation> parsedEquations = parser.parse(equations);
            ms = (System.currentTimeMillis() - ms);
            System.out.println("parsing: " + ms + " ms");
            if (testCaseInd > 0)
                parsingTimes.add(ms);

            ms = System.currentTimeMillis();
            parser2.parse(equations);
            ms = (System.currentTimeMillis() - ms);
            System.out.println("parsing2: " + ms + " ms");
            if (testCaseInd > 0)
                parsingTimes2.add(ms);

        }

        System.out.println("parsing times p80: " + percentile(parsingTimes, 80));
        System.out.println("parsing times2 p80: " + percentile(parsingTimes2, 80));
    }




    public static long percentile(List<Long> times, double Percentile)
    {
        times.sort(Long::compare);
        int Index = (int)Math.ceil(((double)Percentile / (double)100) * (double)times.size());
        return times.get(Index-1);
    }







}
