package com.example.jlabscomp.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class LocalTestCasesStorage {

    private String path = "E:\\utils\\playground\\algos\\jlabsedovx2\\cases";


    public String store(String[] equations){
        try {
            File caseFile = File.createTempFile("case_", "", new File(path));
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper = objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(caseFile, equations);
            return caseFile.getName();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }




    public String[] load(String fileName){
        File file = new File(fileName);
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String[] equations = objectMapper.readValue(file, String[].class);
            return equations;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
