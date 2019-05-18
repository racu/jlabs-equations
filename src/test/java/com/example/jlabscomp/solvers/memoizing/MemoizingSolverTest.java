package com.example.jlabscomp.solvers.memoizing;

import com.example.jlabscomp.solvers.parser.EquationParser;
import com.example.jlabscomp.solvers.recursiveprefixgroup.PrefixGroupSolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class MemoizingSolverTest {

    @Autowired
    MemoizingSolver solver;

    @Autowired
    EquationParser parser;


    @Test
    public void solve() {
        List<String[]> solution = solver.solve(parser.parse(new String[]{"10 ? 3 ? 2 = 4"}));
        System.out.println(Arrays.toString(solution.get(0)));
    }

}