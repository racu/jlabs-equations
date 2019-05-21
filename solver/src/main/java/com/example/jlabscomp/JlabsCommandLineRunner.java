package com.example.jlabscomp;

import com.example.jlabscomp.equationsprocessor.DefaultEquationsProcessor;
import com.example.jlabscomp.jlabsgateway.JlabsGateway;
import com.example.jlabscomp.jlabsgateway.LocalServerGateway;
import com.example.jlabscomp.solvers.parser.ParsedEquation;
import com.example.jlabscomp.solvers.recursive.RecursiveDfsSolver;
import com.example.jlabscomp.solvers.parser.EquationParser;
import com.example.jlabscomp.storage.LocalTestCasesStorage;
import com.example.jlabscomp.verifier.SolutionVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("!test")
public class JlabsCommandLineRunner implements CommandLineRunner {

    @Autowired
    JlabsGateway jlabsGateway;

    @Autowired
    LocalServerGateway localServerGateway;


    @Autowired
    RecursiveDfsSolver solver;

    @Autowired
    LocalTestCasesStorage storage;

    @Autowired
    SolutionVerifier verifier;

    @Autowired
    private EquationParser parser;

    @Autowired
    EquationsUtils eqUtils;


    @Autowired
    DefaultEquationsProcessor eqProcessor;

	@Override
	public void run(String...args) throws Exception {
        warmup(); //few iterations against local server

        testNew();


        //runFinal();

        eqProcessor.shutdown();
	}


    private void testNew(){
        String env = "test";
        DiagnosticMetrics metrics = new DiagnosticMetrics();

        for(int testCaseInd=0; testCaseInd< 1; testCaseInd++) {

            long msTotal = metrics.startTimer();
            long ms = metrics.startTimer();
            EquationsDto testCase = jlabsGateway.retrieveTestCase(env);
            metrics.saveElapsedForMetric("retrieve", ms);

            //storage.store(testCase.equations);

            List<String[]> answers = eqProcessor.solveToListConcurrent(testCase.equations, metrics);

            //verifier.verify(answersToSubmit, parser.parse(testCase.equations));
            ms = metrics.startTimer();
            String response = jlabsGateway.submitListTestCaseAnswers(answers, env);
            metrics.saveElapsedForMetric("submitted", ms);
            metrics.saveElapsedForMetric("total", msTotal);
            System.out.println(response);

        }
        metrics.printPercentiles(60);
        System.out.println();
    }



    private void runFinal(){
        String env = "final";
        DiagnosticMetrics metrics = new DiagnosticMetrics();

        for(int testCaseInd=0; testCaseInd< 1; testCaseInd++) {
            long msTotal = metrics.startTimer();
            EquationsDto testCase = jlabsGateway.retrieveTestCase(env);
            List<String[]> answers = eqProcessor.solveToListConcurrent(testCase.equations, metrics);
            String response = jlabsGateway.submitListTestCaseAnswers(answers, env);
            metrics.saveElapsedForMetric("total", msTotal);
            System.out.println(response);
        }
        metrics.printPercentiles(60);
        System.out.println();
    }

    private void warmup(){
        System.out.println("----warmup----");
        DiagnosticMetrics metrics = new DiagnosticMetrics();

        for(int testCaseInd=0; testCaseInd < 20; testCaseInd++) {

            long msTotal =  metrics.startTimer();

            //retrieve equations
            long ms = metrics.startTimer();
            String[] equations =  localServerGateway.retrieveTestCase().equations;
            ms = metrics.saveElapsedForMetric("retrieving", ms);

            //solve
            List<String[]> answers = eqProcessor.solveToListConcurrent(equations, metrics);

            //submit answers
            ms = metrics.startTimer();
            localServerGateway.submitTestCaseAnswers(answers);  //submit list
            metrics.saveElapsedForMetric("submitted", ms);
            metrics.saveElapsedForMetric("total", msTotal);

            System.out.print(testCaseInd+" ");
        }

        System.out.println();
        metrics.printPercentiles(80);
        System.out.println("----ended warmup----");
        System.out.println();
    }


}
