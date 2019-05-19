package com.example.jlabscomp.verifier;

import com.example.jlabscomp.solvers.parser.ParsedEquation;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SolutionVerifierTest {

    @Test
    public void testValidSolutionPasses(){
        SolutionVerifier ver = new SolutionVerifier();

        List<ParsedEquation> eqs = new ArrayList<>();
        eqs.add(new ParsedEquation(new Integer[]{2,3,4}, 14));

        ArrayList<String[]> solutions = new ArrayList<>();
        solutions.add(new String[]{"+", "*"});
        ver.verify(solutions ,eqs );
    }


    @Test( expected = RuntimeException.class)
    public void testInvalidSolutionFails(){
        SolutionVerifier ver = new SolutionVerifier();

        List<ParsedEquation> eqs = new ArrayList<>();
        eqs.add(new ParsedEquation(new Integer[]{2,3,4}, 14));

        ArrayList<String[]> solutions = new ArrayList<>();
        solutions.add(new String[]{"+", "+"});
        ver.verify( solutions, eqs);
    }
}