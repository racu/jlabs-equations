package com.example.jlabscomp.solvers.memoizing;

import com.example.jlabscomp.solvers.parser.EquationParser;
import com.example.jlabscomp.solvers.recursiveprefixgroup.PrefixGroupSolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
        String[] solution = solver.solve(parser.parse(new String[]{"10 ? 3 ? 2 = 4"}));
        System.out.println(solution[0]);
    }

}