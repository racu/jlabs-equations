package com.example.server;

import com.example.jlabscomp.solvers.parser.FastEquationParser;
import com.example.jlabscomp.solvers.parser.ParsedEquation;
import com.example.jlabscomp.storage.LocalTestCasesStorage;
import com.example.jlabscomp.verifier.SolutionVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CompetitionServerController {

    @Autowired
    LocalTestCasesStorage storage;

    @Autowired
    SolutionVerifier verifier;

    @Autowired
    FastEquationParser parser;

    EquationsDto equationsDto;
    List<ParsedEquation> parsedEquations;


    String caseFilePath = "E:\\utils\\playground\\algos\\jlabsedovx2\\cases\\performance1\\case_1353454811441596726";

    //String caseFilePath167MB = "E:\\utils\\playground\\algos\\jlabsedovx2\\cases\\huge\\case_7919375993971561685";


    @RequestMapping(value= "getEquations")
    public EquationsDto getEquationsToSolve(){
        if(equationsDto == null) {
            String[] equations = storage.load(caseFilePath);
            equationsDto = new EquationsDto();
            equationsDto.equations = equations;
            parsedEquations = parser.parse(equationsDto.equations);
        }

        System.out.println("returning "+equationsDto.equations.length+" equations");
        return equationsDto;
    }

    @RequestMapping(value= "submitSolutions")
    public void submitSolutions(@RequestBody List<String[]> solutions){
        System.out.println("submitted "+solutions.size()+" solutions");
       // verifier.verify(solutions, parsedEquations);
    }
}
