package com.example.jlabscomp.storage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LocalTestCasesStorageTest {

    @Autowired
    LocalTestCasesStorage store;

    @Test
    public void simpleTest(){
        store.store(new String[]{"A","B","C"});;
    }
}