package com.example.jlabscomp.solvers;

import com.example.jlabscomp.solvers.recursiveprefixgroup.PrefixGroupSolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class PrefixGroupSolverTest {

    @Autowired
    PrefixGroupSolver solver;

    @Test
    public void solve() {
        //String[] solution = solver.solve(new String[]{"74 ? 59 ? 9 ? 22 = 213"});
        //String[] solution = solver.solve(new String[]{"7 ? 7 ? 4 ? 4 = 33"});
        String[] solution = solver.solve(new String[]{"10 ? 3 ? 3 = 19"});

        System.out.println(solution[0]);
    }

    @Test
    public void solveLargeNumbers() {

        String[] solution = solver.solve(new String[]{"100 ? 100 ? 100 ? 100 ? 100 = 10000000000"});
        System.out.println(solution[0]);
    }
}