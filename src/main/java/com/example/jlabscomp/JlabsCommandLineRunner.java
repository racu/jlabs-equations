package com.example.jlabscomp;

import com.example.jlabscomp.jlabsgateway.JlabsGateway;
import com.example.jlabscomp.solvers.parser.ParsedEquation;
import com.example.jlabscomp.solvers.recursiveprefixgroup.PrefixGroupSolver;
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
    PrefixGroupSolver solver;

    @Autowired
    LocalTestCasesStorage storage;

    @Autowired
    SolutionVerifier verifier;

    @Autowired
    private EquationParser parser;

    @Autowired
    EquationsUtils eqUtils;

	@Override
	public void run(String...args) throws Exception {

	    String env = "test";


	    for(int testCaseInd=0; testCaseInd<2; testCaseInd++) {

            EquationsDto testCase = jlabsGateway.retrieveTestCase(env);

            storage.store(testCase.equations);

            long ms = System.currentTimeMillis();

            //parse input
            List<ParsedEquation> parsedEq = parser.parse(testCase.equations);

            //solve
            String[] answers = solver.solve(parsedEq);
            System.out.println("solved: " + (System.currentTimeMillis() - ms) + " ms");

            ms = System.currentTimeMillis();

            List<String[]> answersToSubmit = eqUtils.convertResultsToSubmittableOutput(answers);
            System.out.println("formatted: " + (System.currentTimeMillis() - ms) + " ms");

            //verifier.verify(answersToSubmit, parser.parse(testCase.equations));

            jlabsGateway.submitTestCaseAnswers(answersToSubmit, env);

        }

	}




}
