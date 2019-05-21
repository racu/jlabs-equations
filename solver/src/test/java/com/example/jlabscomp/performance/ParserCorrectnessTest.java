package com.example.jlabscomp.performance;

import com.example.jlabscomp.solvers.parser.EquationParser;
import com.example.jlabscomp.solvers.parser.FastEquationParser;
import com.example.jlabscomp.solvers.parser.ParsedEquation;
import com.example.jlabscomp.storage.LocalTestCasesStorage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ParserCorrectnessTest {

    String performanceStorePath = "E:\\utils\\playground\\algos\\jlabsedovx2\\cases\\performance1\\case_1353454811441596726";

    @Autowired
    LocalTestCasesStorage store;

    @Autowired
    EquationParser oldParser;

    @Autowired
    FastEquationParser parser;

    @Test
    //@Ignore
    public void testCorrectness(){
        String[] equations = store.load(performanceStorePath);
        List<ParsedEquation> equations1 = oldParser.parse(equations);
        List<ParsedEquation> equations2 = parser.parse(equations);
        for(int eqI=0;eqI<equations1.size(); eqI++){
            ParsedEquation parsedEquation1 = equations1.get(eqI);
            ParsedEquation parsedEquation2 = equations2.get(eqI);
            Assert.assertArrayEquals(parsedEquation1.getValues(),parsedEquation2.getValues());
            Assert.assertEquals(parsedEquation1.getRes(),  parsedEquation2.getRes());
        }
    }

}
