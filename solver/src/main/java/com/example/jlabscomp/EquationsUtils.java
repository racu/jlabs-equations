package com.example.jlabscomp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Component
public class EquationsUtils {

    @SneakyThrows
    public List<String[]> convertResultsToSubmittableOutput(String[] results){
        List<String[]> answersToSubmit = new ArrayList<>(results.length);
        for (String ans : results) {
            String[] operators = new String[ans.length()];
            for(int i=0; i<ans.length(); i++)
                operators[i] = Character.toString(ans.charAt(i));
            answersToSubmit.add(operators);
        }
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValueAsString(results);
        return answersToSubmit;
    }

    public String convertResultsToSubmittableOutputString(String[] results){
        StringBuilder outputText = new StringBuilder(1000000);
        outputText.append("[");
        for(int ansI=0; ansI<results.length; ansI++){
            char[] chars = results[ansI].toCharArray();
            outputText.append("[");
//            StringJoiner joiner = new StringJoiner(",", "[", "]");
//            for(int i=0; i<chars.length - 1; i++)
//                joiner.add(String.valueOf(chars[i]));
            for(int i=0; i<chars.length - 1; i++)
                outputText.append(chars[i] +",");
            outputText.append("\"" + chars[chars.length - 1] + "\"]");
            if(ansI < results.length-1)
                outputText.append(",");
        }
        outputText.append("]");
        return outputText.toString();
    }


    public String convertResultsToSubmittableOutputString(List<String[]> results){
        StringBuilder outputText = new StringBuilder(1000000);
        outputText.append("[");
        String[] answer;

        for(int ansI=0; ansI<results.size(); ansI++){
            answer = results.get(ansI);

            outputText.append("[\"");
            for(int i=0; i<answer.length-1; i++)
                outputText.append(answer[i]+"\",\"");
            outputText.append(answer[answer.length-1]+"\"]");

//            StringJoiner joiner = new StringJoiner(",", "[", "]");
//            for(int i=0; i<answer.length; i++)
//                joiner.add("\""+answer[i]+"\"");
//            outputText.append(joiner.toString());

            if(ansI < results.size() - 1)
                outputText.append(",");
        }
        outputText.append("]");
        return outputText.toString();
    }

    //convert to submitable string
}
