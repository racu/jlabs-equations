package com.example.jlabscomp.performance;


import com.example.jlabscomp.EquationsDto;
import com.example.jlabscomp.EquationsUtils;
import com.example.jlabscomp.jlabsgateway.JlabsGateway;
import com.example.jlabscomp.solvers.memoizing.MemoizingSolver;
import com.example.jlabscomp.solvers.parser.EquationParser;
import com.example.jlabscomp.solvers.parser.FastEquationParser;
import com.example.jlabscomp.solvers.parser.ParsedEquation;
import com.example.jlabscomp.solvers.recursiveprefixgroup.PrefixGroupSolver;
import com.example.jlabscomp.storage.LocalTestCasesStorage;
import com.example.jlabscomp.verifier.SolutionVerifier;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
public class Utils {

    @Autowired
    LocalTestCasesStorage store;

    @Autowired
    JlabsGateway jlabsGateway;

    @Autowired
    MemoizingSolver solver;

    @Autowired
    LocalTestCasesStorage storage;

    @Autowired
    SolutionVerifier verifier;

    @Autowired
    FastEquationParser parser;

    @Autowired
    EquationsUtils eqUtils;


    @Test
    public void collectSamplesFromJlabs(){
        String env = "test";

        for(int testCaseInd = 0; testCaseInd < 1000; testCaseInd++) {

            //retrieve test case
            EquationsDto testCase = jlabsGateway.retrieveTestCase(env);

            //parse
            List<ParsedEquation> parsedEq = parser.parse(testCase.equations);

            //solve
            List<String[]> answers = solver.solve(parsedEq);

            //convert answers to string
            String answersToSubmit = eqUtils.convertResultsToSubmittableOutputString(answers);

            //submit
            HttpStatus httpStatus = jlabsGateway.submitTestCaseAnswers(answersToSubmit, env);

            //verify
            //verifier.verify(answers, parsedEq);

            System.out.println("submission: "+httpStatus);
            System.out.println("iteration: "+testCaseInd);
            storage.store(testCase.equations);
        }
    }




    @SneakyThrows
    @Test
    public void collectFilesFromDir(){

        String dir = "E:\\utils\\playground\\algos\\jlabsedovx2\\cases";

        List<String> allEquations = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(Paths.get(dir))) {
            List<String> files = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            for(String fPath : files) {
                String[] equations = store.load(fPath);
                allEquations.addAll(Arrays.asList(equations));
            }
        }
        store.store(allEquations.toArray(new String[0]));
    }

}
