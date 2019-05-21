package com.example.jlabscomp.solvers.parser;

import org.junit.Assert;
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
public class FastEquationParserTest {

    @Autowired
    FastEquationParser parser;

    @Test
    public void testParsing3Values(){
        ParsedEquation parsed = parser.parse("1 * 2 * 3 = 4");
        Assert.assertTrue(parsed.res == 4);
        Assert.assertTrue(parsed.values.length == 3);
        Assert.assertTrue(parsed.values[0] == 1);
        Assert.assertTrue(parsed.values[1] == 2);
        Assert.assertTrue(parsed.values[2] == 3);
    }

    @Test
    public void testParsing4Values(){
        ParsedEquation parsed = parser.parse("1 * 2 * 3 * 4 = 5");
        Assert.assertTrue(parsed.res == 5);
        Assert.assertTrue(parsed.values.length == 4);
        Assert.assertTrue(parsed.values[0] == 1);
        Assert.assertTrue(parsed.values[1] == 2);
        Assert.assertTrue(parsed.values[2] == 3);
        Assert.assertTrue(parsed.values[3] == 4);
    }

    @Test
    public void testParsing5BiggerValues(){
        ParsedEquation parsed = parser.parse("16 * 25 * 33 * 47= 5");
        Assert.assertTrue(parsed.res == 5);
        Assert.assertTrue(parsed.values.length == 4);
        Assert.assertTrue(parsed.values[0] == 1);
        Assert.assertTrue(parsed.values[1] == 2);
        Assert.assertTrue(parsed.values[2] == 3);
        Assert.assertTrue(parsed.values[3] == 4);
    }
}